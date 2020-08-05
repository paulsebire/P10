# bibliotheque

## Projet 7 OC Gestion d'une bibliothèque d'une grande ville

Description :
  
###  bibliotheque est composé de 3 modules :  
  
                    Un Module : microservice-clientui
                    Un Module : microservice-books
                    Un Module : microservice-utilisateur
                    
###  Api Gateway :
 
                    Zuul : zuul-server

###  Edge services utilisés : 
                    
                    Spring-Cloud-Config : config-server
                    Eureka : Eureka-server
                    Zipkin

### Lancer l'application : 
                    
                    mvn spring-boot:run

### Etapes : 

      Ordre de déploiement
      Avant toute chose, il est necessaire de créer les bases de données(PostgreSQL):
              [Veillez a respecter le nom des bases de données]
              -bibliotheque: pour les resrvations, les copies, les livres
              -bilbioutilisateur: pour les utilisateurs

### Etape 1 : 
      
       1  Zipkin-server
      
### Etape 2 : 
      
       1  config-server
       2  eureka-server
       
### Etape 3 : 
      
       1  zuul-server
       
### Etape 4 : 
      
       1  microservice-utilisateur
            import de la base de donnée (opération non nécessaire):
            psql -U postgres biblioutilisateur < /schema_biblioutilisateur.sql
            psql -U postgres biblioutilisateur < /data_biblioutilisateur.sql
       2  microservice-books
            import de la base de donnée (opération non nécessaire):
            psql -U postgres biblioutilisateur < /schema_bibliotheque.sql
            psql -U postgres biblioutilisateur < /data_bibliotheque.sql
       3  microservice-clientui
       
### comment générer la javadoc :

           - Dans le panneau de contrôle de maven en lancant la commande: javadoc:javadoc
           - Dans Intellij via le menu Tools puis Generate Javadoc