# ffm-java-22-1-capstone

This is my graduation project from the ffm-java-22-1 course.

It's a library for BPMN-Diagrams from a Camunda Engine. You can fetch them from the engine or add custom ones, see the
version history and other Details, and add comments to every BPMN-diagram.

Requirements to run:

- start MongoDB on Port 27017 (MongoDB Database name is "BpmnLibrary")

- to get BPMN-diagrams from Camunda Engine, property camunda.api.baseUrl needs to be set to URL of Camunda REST API:
  camunda.api.baseUrl=".../engine-rest"

- run `npm install` in frontend-folder

- start `BackendApplication.java`

- run `npm start`



