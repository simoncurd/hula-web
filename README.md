# Hula-web

Hula-web is a web application framework for Java based on the [Hula Scripting Language](https://github.com/simoncurd/hula-lang). It was designed to simplify and accelerate  development of web applications.

Benefits:

* Simplicity - The simplicity of the Hula language makes developing and maintaining web applications easy.
* Suitability - Hula-based scripts are well suited to request-based interactions of web applications.
* Separation - Hula introduces a clear separation between service and application layers of an application.

# Principles

Hula-web applications are composed of Scripts & Pages.

* Scripts - controls the request, reading request parameters, calling services, handling the session and specifying page flow.
* Pages - Velocity templates which are used to render the view.

The Hula Web framework relates well to the Model-View-Controller (MVC) paradigm, in that Scripts behave as the Controller, Pages provide the Views and the object graphs of incoming requests and service interactions are the Model.

# Getting Started

A war file is provided in the /release folder which can be dropped straight into Tomcat. This deploys to the /hula context.

Once deployed:
* The homepage for hula can be reached at [http://localhost:8080/hula/welcome](http://localhost:8080/hula/welcome) (Assuming out-the-box Tomcat configuration).

# Latest Version

* This is a very early release and is not considered production-ready.
* The current release is version 0.1.1

# Roadmap

Key Features for future versions:

* HttpUnit-based tests
* Validation framework
* Improved web-service support
* SEO URL support

# Contributing

* Please raise any issues in the github issue tracker

# Author

* http://github.com/simoncurd

# Copyright and License

[Apache 2 License]

Copyright 2013 Simon Curd simoncurd@gmail.com

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0]

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
