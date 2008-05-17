/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.argo.language;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.uml.generator.CodeGenerator;
import org.argouml.uml.generator.GeneratorHelper;
import org.argouml.uml.generator.GeneratorManager;
import org.argouml.uml.generator.Language;
import org.argouml.uml.generator.SourceUnit;
import org.dbuml.base.controller.ActionsInterface;
import org.dbuml.base.factory.Factory;
import org.dbuml.base.i18n.Translator;
import org.dbuml.base.model.DBModelFacade;

/**************************************************************************
 *  GeneratorSQL is called by:
 *  - Argo in TabSrc.genText() to generate the source text property tab when SQL
 *  notation is choosen.
 *  - Argo in ClassGenerationDialog.actionPerformed() when SQL notation choosen
 * for source file generation.
 *  - DBUML in GenericActions.generateSQL when a generate source popup 
 * menu item is selected. *
 **************************************************************************/

public class GeneratorSQL implements CodeGenerator {
    /*****************
     *      Attributes
     *****************/
    
    private static final GeneratorSQL SINGLETON = new GeneratorSQL();
    private static final Logger LOG = Logger.getLogger(GeneratorSQL.class);
    
    /*******************
     *      Constructors
     *******************/
    
    private GeneratorSQL() {
        NotationName nn =
                Notation.makeNotation(
                "SQL",
                null,
                ResourceLoaderWrapper.lookupIconResource("JavaNotation"));
        String cv = nn.getConfigurationValue();
        Language lang =
                GeneratorHelper.makeLanguage(cv, nn.getTitle(), nn.getIcon());
        GeneratorManager.getInstance().addGenerator(lang, this);
    }
    
    /************************
     *      My Methods
     ************************/
    
    /**
     * Get the instance of the singleton for the generator.
     *
     * @return the singleton of the generator.
     */
    public static GeneratorSQL getInstance() {
        return SINGLETON;
    }
    
    /*
     *  generateSource is called directly by DBUML in 
     * GenericActions.generateSource
     * and is used by the methods below implementing the CodeGenerator
     interface
     */
    private String generateSource(Object obj) {
        Factory fac = DBModelFacade.getInstance().getMyFactory(obj);
        String retVal = null;
        if (fac != null) {
            ActionsInterface actionsinterface = fac.getActionsInterface();
            retVal = actionsinterface.generateSource(obj, false, true);
        } else {
            retVal = Translator.getInstance().localize("GENSQL_FAILURE_TITLE") 
                    + Translator.getInstance().localize("DB_FACTORY_NOTFOUND");
        }
        return retVal;
    }
    
    /**
     * Generates the relative path for the specified classifier.
     * @param cls The classifier.
     * @return Returns relative path of cls (without filename).
     */
    private String generatePath(Object cls) {
        String packagePath =
            generateRelativePackage(cls, null,
                CodeGenerator.FILE_SEPARATOR);
        packagePath = packagePath.substring(1);
        return packagePath;
    }
    
    /**
     * Generate the package name for the specified object,
     * relative to the specified package. Use sep as the
     * package separator.
     * @param cls Object to generate the path for
     * @param pack Generate path relative to this package
     * @param sep package separator
     * @return path relative to pack, if pack is a parent of
     *         cls, else relative to the project root. If the
     *         path is relative to the project root, it's prefixed
     *         with sep.
     */
    private String generateRelativePackage(Object cls, Object pack,
            String sep) {
        StringBuffer packagePath = new StringBuffer();
        // avoid model being used as a package name
        Object parent = Model.getFacade().getNamespace(cls);
        
        while (parent != null && parent != pack) {
            // ommit root package name; it's the model's root
            Object grandParent = Model.getFacade().getNamespace(parent);
            if (grandParent != null) {
                String name = Model.getFacade().getName(parent);
                if (packagePath.length() > 0) {
                    packagePath.insert(0, sep);
                }
                packagePath.insert(0, name);
            }
            parent = grandParent;
        }
        if (parent == null) { // relative to root, prefix with sep
            packagePath.insert(0, sep);
        }
        return packagePath.toString();
    }
    
    /*
     * Generate files for element 'o'
     * Return the collection of files (as Strings).
     */
    private Collection<String> generateFilesForElem(Object o,
            String path) {
        List<String> ret = new ArrayList<String>();
           
        String pathname = createDirectoriesPathname(o, path);
        
        String fileContent = generateSource(o); 
        
        if (fileContent.length() != 0) {
            BufferedWriter fos = null;
            File f = new File(pathname);
            try {
                fos = new BufferedWriter(new FileWriter(f));
                fos.write(fileContent);
            } catch (IOException exp) {
                System.err.println("Ignored " + exp.getMessage());   
            } finally {
                try {
                    if (fos != null) { fos.close(); }
                } catch (IOException exp) {
                    LOG.error("FAILED: " + f.getPath());
                }
            }
            LOG.info("written: " + pathname);
            ret.add(pathname);
        }
        return ret;
    }
    
    /**
     * create the needed directories for the derived appropriate pathname
     * @return Returns the filename with full path of cls.
     */
    private String createDirectoriesPathname(Object cls, String path) {
        String name = Model.getFacade().getName(cls);
        
        String pathname = "";
        if (name != null && name.length() > 0) {
            if (!path.endsWith(CodeGenerator.FILE_SEPARATOR)) {
                path += CodeGenerator.FILE_SEPARATOR;
            }

            String packagePath = generateRelativePackage(cls, null, ".");
            packagePath = packagePath.substring(1);
            String filename = name + ".sql";

            int lastIndex = -1;
            do {
                File f = new File(path);
                if (!f.isDirectory()) {
                    if (!f.mkdir()) {
                        LOG.error(" could not make directory " + path);
                        path = null;
                        break;
                    }
                }

                if (lastIndex == packagePath.length()) {
                    break;
                }

                int index = packagePath.indexOf(".", lastIndex + 1);
                if (index == -1) {
                    index = packagePath.length();
                }

                path += packagePath.substring(lastIndex + 1, index)
                    + CodeGenerator.FILE_SEPARATOR;
                lastIndex = index;
            } while (true);
            if (pathname != null) {
                pathname = path + filename;
            }
        }
        //LOG.info("-----" + pathname + "-----");
        return pathname;
    }
    
    /**
     * CodeGenerator interface
     * @param elements 
     * @param deps 
     * @return 
     */
    
    /*
     * @see org.argouml.uml.generator.CodeGenerator#generate(java.util.Collection, boolean)
     */
    /**
     * Generate code for the specified classifiers. If generation of
     * dependencies is requested, then every file the specified elements
     * depends on is generated too (e.g. if the class MyClass has an attribute
     * of type OtherClass, then files for OtherClass are generated too).
     *
     * @param elements the UML model elements to generate code for.
     * @param deps Recursively generate dependency files too.
     * @return A collection of {@link SourceUnit} objects. The collection
     *         may be empty if no file is generated.
     */
    public Collection generate(Collection elements, boolean deps) {
        List ret = new ArrayList();
        for (Object elem : elements) {
            String path = generatePath(elem);
            String name = Model.getFacade().getName(elem) + "sql";
            String content = generateSource(elem); 
            SourceUnit su = new SourceUnit(name, path, content);
            ret.add(su);
        }
        return ret;
    }
    
    /*
     * @see org.argouml.uml.generator.CodeGenerator#generateFileList(java.util.Collection, boolean)
     */
    /**
     * Returns a list of files that will be generated from the specified
     * modelelements.
     * @see #generate(Collection, boolean)
     * @param elements the UML model elements to generate code for.
     * @param deps Recursively generate dependency files too.
     * @return The filenames (with relative path) as a collection of Strings.
     * The collection may be empty if no file will be generated.
     */
    public Collection generateFileList(Collection elements, boolean deps) {
        List ret = new ArrayList();
        for (Object elem : elements) {
            ret.add(Model.getFacade().getName(elem) + ".sql");
        }
        return null;
    }
    /*
     * @see org.argouml.uml.generator.CodeGenerator#generateFiles(java.util.Collection, java.lang.String, boolean)
     */
    /**
     * Generate files for the specified classifiers.
     * @see #generate(Collection, boolean)
     * @param elements the UML model elements to generate code for.
     * @param path The source base path.
     * @param deps Recursively generate dependency files too.
     * @return The filenames (with relative path) as a collection of Strings.
     * The collection may be empty if no file will be generated.
     */
    public Collection generateFiles(Collection elements, String path,
            boolean deps) {
        List ret = new ArrayList();
        for (Object elem : elements) {
            ret.addAll(generateFilesForElem(elem, path));
        }
        return ret;
    }
}
