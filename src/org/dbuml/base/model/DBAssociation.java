/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/

package org.dbuml.base.model;

/**
 * Class for modeling an association.
 * @author jgrengbondai
 */
public class DBAssociation extends DBElement {
    
    private Object sourceModel;
    private String sourceEndName;
    private String sourceEndStereo;
    
    private Object targetModel;
    private String targetEndName;
    private String targetEndStereo;
    
    private static final String MULTIPLICITY_EXACTLY_ONE = "M1_1";
    private static final String MULTIPLICITY_ATMOST_ONE = "M0_1";
    private static final String MULTIPLICITY_ATLEAST_ONE = "M1_N";
    private static final String MULTIPLICITY_ZERO_OR_MORE = "M0_N";
    
    // should the default be 1-1 or 0-*, how do we know it?
    private String targetMultiplicity = MULTIPLICITY_EXACTLY_ONE;
    private String sourceMultiplicity = MULTIPLICITY_EXACTLY_ONE;
    
    private boolean targetOrdering = false;
    private boolean sourceOrdering = false;
    
    
    /**
     * Creates a new instance of DBAssociation.
     */
    public DBAssociation() {
    }
    
    /**
     * Creates a new instance of DBAssociation.
     * @param name The name of the association.
     */
    public DBAssociation(final String name) {
        super(name);
    }
    
    /**
     * Gets the string for the stereotype.
     * @return The string for the stereotype or null.
     */
    public String getStereostring() {
        return null;
    }
    
//    /**
//     * Sets the model element.
//     * @param objModel The model element.
//     */
//    public void setModelElement(final Object objModel) {
//        super.setModelElement(objModel);
//        //TO DO: can we fish the end points out of the association 
//        //and set them?
//    }
    
    /**
     * Sets the model elements.
     * @param objModel The association model.
     * @param sModel The source model.
     * @param tModel The target model.
     */
    public void setModelElement(
            final Object objModel,
            final Object sModel,
            final Object tModel) {
        super.setModelElement(objModel);
        this.setSourceModel(sModel);
        this.setTargetModel(tModel);
    }
    
    /**
     * Sets the source model.
     * @param sModel The source model.
     */
    public void setSourceModel(final Object sModel) {
        this.sourceModel = sModel;
        // TO DO: Make sure that this association is really attached to this 
        // source.
    }
    
    /**
     * Gets the source model.
     * @return The source model.
     */
    public Object getSourceModel() {
        return this.sourceModel;
    }
    
    /**
     * Sets sourceEnd name.
     * @param sEndName The name of the source end.
     */
    public void setSourceEndName (final String sEndName) {
        this.sourceEndName = sEndName;
    }
    
    /**
     * Gets source end name.
     * @return The name of the source end.
     */
    public String getSourceEndName() {
        return this.sourceEndName;
    }
    
    /**
     * Sets the stereotype string of the source end.
     * @param sEndStereo Source end stereotype.
     */
    public void setSourceEndStereoString(final String sEndStereo) {
        this.sourceEndStereo = sEndStereo;
    }
    
    /**
     * Gets the stereotype string of the source end.
     * @return The stereotype string of the source end.
     */
    public String getSourceEndStereoString() {
        return this.sourceEndStereo;
    }
    
    /**
     * Gets the target model.
     * @param tModel The target model.
     */
    public void setTargetModel(final Object tModel) {
        this.targetModel = tModel;
        // TO DO: Make sure that this association is really attached to 
        // this source. It should have the navigable end of the association
    }
    
    /**
     * Gets the model of the target.
     * @return The target model.
     */
    public Object getTargetModel() {
        return this.targetModel;
    }
    
    /**
     * Sets the name of the terget end.
     * @param tEndName The targetEnd name.
     */
    public void setTargetEndName (final String tEndName) {
        this.targetEndName = tEndName;
    }
    
    /**
     * Gets the targetEnd name.
     * @return The target end name.
     */
    public String getTargetEndName() {
        return this.targetEndName;
    }
    
    /**
     * Sets the stereotype string for the target end.
     * @param tEndStereo The target end stereo type string.
     */
    public void setTargetEndStereoString(final String tEndStereo) {
        this.targetEndStereo = tEndStereo;
    }
    
    /**
     * Gets the stereotype string of the target end.
     * @return The stereotype string of the target end.
     */
    public String getTargetEndStereoString() {
        return this.targetEndStereo;
    }
    
    /**
     * Sets the multiplicity of the target to be exactly one.
     */
    public void setTargetMultiplicityExactlyOne() {
        this.targetMultiplicity = MULTIPLICITY_EXACTLY_ONE;
    }
    
    /**
     * Sets the multiplicity of the source to be exactly one.
     */
    public void setSourceMultiplicityExactlyOne() {
        this.sourceMultiplicity = MULTIPLICITY_EXACTLY_ONE;
    }
    
    /**
     * Determine whether or not the multiplicity of the target is exactly one.
     * @return true or false.
     */
    public boolean isTargetMultiplicityExactlyOne() {
        return (this.targetMultiplicity == MULTIPLICITY_EXACTLY_ONE);
    }
    
    /**
     * Determine whether or not the multiplicity of the source is exactly one.
     * @return true or false.
     */
    public boolean isSourceMultiplicityExactlyOne() {
        return (this.sourceMultiplicity == MULTIPLICITY_EXACTLY_ONE);
    }
    
    /**
     * Sets the multiplicity of the target to be no more than one.
     */
    public void setTargetMultiplicityAtMostOne() {
        this.targetMultiplicity = MULTIPLICITY_ATMOST_ONE;
    }
    
    /**
     * Sets the multiplicity of the source to be no more than one.
     */
    public void setSourceMultiplicityAtMostOne() {
        this.sourceMultiplicity = MULTIPLICITY_ATMOST_ONE;
    }
    
    /**
     * Determines whether or not the multiplicity of the target is no more 
     * than one.
     * @return true or false.
     */
    public boolean isTargetMultiplicityAtMostOne() {
        return (this.targetMultiplicity == MULTIPLICITY_ATMOST_ONE);
    }
    
    /**
     * Determines whether or not the multiplicity of the source is no more 
     * than one.
     * @return true or false.
     */
    public boolean isSourceMultiplicityAtMostOne() {
        return (this.sourceMultiplicity == MULTIPLICITY_ATMOST_ONE);
    }
    
    /**
     * Sets the multiplicity of the target to be one or more.
     */
    public void setTargetMultiplicityAtLeastOne() {
        this.targetMultiplicity = MULTIPLICITY_ATLEAST_ONE;
    }
    
    /**
     * Sets the multiplicity of the source to be one or more.
     */
    public void setSourceMultiplicityAtLeastOne() {
        this.sourceMultiplicity = MULTIPLICITY_ATLEAST_ONE;
    }
    
    /**
     * Determines whether or not the multiplicity of the target is one or more.
     * @return true or false.
     */
    public boolean isTargetMultiplicityAtLeastOne() {
        return (this.targetMultiplicity == MULTIPLICITY_ATLEAST_ONE);
    }
    
    /**
     * Determines whether or not the multiplicity of the source is one or more.
     * @return true or false.
     */
    public boolean isSourceMultiplicityAtLeastOne() {
        return (this.sourceMultiplicity == MULTIPLICITY_ATLEAST_ONE);
    }
    
    /**
     * Sets the multiplicity of the target to be zero or more.
     */
    public void setTargetMultiplicityZeroOrMore() {
        this.targetMultiplicity = MULTIPLICITY_ZERO_OR_MORE;
    }
    
    /**
     * Sets the multiplicity of the source to be zero or more.
     */
    public void setSourceMultiplicityZeroOrMore() {
        this.sourceMultiplicity = MULTIPLICITY_ZERO_OR_MORE;
    }
    
    /**
     * Determines whether or not the multiplicity of the target is zero or more.
     * @return true or false.
     */
    public boolean isTargetMultiplicityZeroOrMore() {
        return (this.targetMultiplicity == MULTIPLICITY_ZERO_OR_MORE);
    }
    
    /**
     * Determines whether or not the multiplicity of the source is zero or more.
     * @return true or false.
     */
    public boolean isSourceMultiplicityZeroOrMore() {
        return (this.sourceMultiplicity == MULTIPLICITY_ZERO_OR_MORE);
    }
    
    /**
     * Sets that the target is ordered.
     * @param ordered true for an ordered target, and false otherwise.
     */
    public void setTargetOrdering(boolean ordered) {
        this.targetOrdering = ordered;
    }
    
    /**
     * Sets that the source is ordered.
     * @param ordered true for an ordered target, and false otherwise.
     */
    public void setSourceOrdering(boolean ordered) {
        this.sourceOrdering = ordered;
    }
    
    /**
     * Is the target ordered.
     * @return ture or false.
     */
    public boolean isTargetOrdered() {
        return this.targetOrdering;
    }
    
    /**
     * Is the target ordered.
     * @return ture or false.
     */
    public boolean isSourceOrdered() {
        return this.sourceOrdering;
    }
    
}
