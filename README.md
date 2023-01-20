# Weather Data Analysis
This project is a web application developed using the MVC pattern which is intended to store weather data into a database and provide statistical analysis based on them.

This project is part of a bigger one which has the purpose to make MVC observable. In order to do so the MVC pattern has been modified into another version named oMVC. This latter version of the same project can be found [here](https://github.com/nicologhielmetti/weatherdatanalysis_oMVC).

Moreover, this project, which was initially built using a MySQL database (accessed via Hibernate), has been revisited experimenting NoSQL solutions. The solutions taken into account concern a graph based database and a document based database: Neo4j and MongoDB respectively. The implementation of both versions can be found in the [graph-db](https://github.com/francescoalongi/weatherdataanalysis_MVC/tree/graph-db) and in the [document-db](https://github.com/francescoalongi/weatherdataanalysis_MVC/tree/document-db) branches. Additionally, in order to fairly compare the three database technologies taken into account, the version considered in the current branch has been modified removing the Hibernate middle layer. This last version can be found in the [relational-db](https://github.com/francescoalongi/weatherdataanalysis_MVC/tree/relational-db) branch.

## Class diagram
The following diagram shows the structure of this project.

![Class_diagram](https://user-images.githubusercontent.com/19633559/79385123-5d7cae80-7f68-11ea-85da-52f82dc61489.jpg)


## Built With

* [MySQL](https://www.mysql.com/) - RDBMS used
* [Hibernate](https://hibernate.org/orm/documentation/5.4/) - Used to handle the object-relational impedance mismatch
* [Maven](https://maven.apache.org/) - Dependency Management

## Further details
If you wish to know more about this project check the [related paper](https://doi.org/10.1002/spe.3116) which have been published on Wiley Online Library.
