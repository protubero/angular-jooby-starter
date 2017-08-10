# Angular Jooby Starter

This starter repo serves as an Angular starter for anyone looking to get up and running with Angular and TypeScript and a Java server fast. 
I'm using [jooby](https://github.com/jooby-project/jooby) on the server side as it provides amenities like hot reload and [swagger](https://swagger.io/) integration. Especially the hot reload feature is essential for coding an angular application.

## Quick start

1. Checkout the starter project with `git clone https://github.com/protubero/angular-jooby-starter.git`
1. Make sure you have a Node and NPM, the node package manager, installed on your system. Minimum version of Node is 4, minimum version of NPM is 3. It is sufficient to install Node on your system since NPM is installed together with Node.   
1. Install [Angular CLI](https://github.com/angular/angular-cli)
1. You should have a running maven installation on your system
1. To start the java server, execute `mvn jooby:run` at the *server* sub folder. The jooby server starts on port 8080. When opening (http://localhost:8080/api/persons), a json structure with some person data should show up. All available URLs are listed on the console when the server starts up. 
1. Execute `npm update` at the *client* folder. NPM is looking for the modules defined in the *package.json* file and resolves all dependencies. This could take some time. A folder *node_modules* will be created which contains the JS libaries used.
1. To start the client, execute `npm start` at the *client* folder, then open (http://localhost:4200) in your browser. Calls to the backend are routed to the jooby server, as defined in the *proxy.conf.json* file.

Now you have the development setup up and running.

Recommended IDE: https://www.genuitec.com/products/angular-ide/
 

## Build distribution  
 
The project is structured as a multi-module maven project. Beside the server and client module there is a third module named distribution. 
It contains a maven assembly descriptor that packs together the server and client into one zip file and a tarball. To build the distribution, execute `mvn package` in the root folder. The server is accessible via (http://localhost:8080).

The angular client will then be generated in production mode as a set of files located in the *dist/client* folder. These files are then packaged into a jar file that is the artifact of that module.
	
## Playing around with docker (work in progress)

### Building the distribution with docker

Execute `docker build . -t ajs` in the project root folder.


## Coverage check of Angular features

https://angular.io/guide/architecture

Building blocks
    Modules
    Components
    Templates
    Metadata
    Data binding
    Directives
    Services
    Dependency injection

	
    Animations: 
    Change detection
    Events
    Forms
    HTTP
    Lifecycle hooks
    Pipes
    Router
    Testing



	
