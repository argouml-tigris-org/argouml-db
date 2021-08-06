# UML for relational databases

Allows forward and reverse engineering.   

## How to build

Project uses Apache ant:

- ant

## How to run

- prepare `database.properties` from `database.properties.sample`
- ant run

# How to use

For reverse engineering 

- open `Tools->New Database Deployment Diagram`   
- create figure "New Database"
- right-click on this figure and select "Connect to catalog"
- you will be connected to your database
- if not check values in <<Database>> tagged values
- right-click on this figure and select "Import from catalog"
- import schema and types
- right-click on this figure and select "Add dependencies from"
- add dependency from your schema
- it will create a figure with schema
- right click ot this figure and select "Import tables"
- open `Tools->New Database Schema Diagram`
- use it to drag and drop schema, types, tables and views 





