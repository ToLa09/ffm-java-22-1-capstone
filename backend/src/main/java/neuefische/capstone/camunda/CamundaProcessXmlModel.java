package neuefische.capstone.camunda;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CamundaProcessXmlModel(String id, @JsonProperty("bpmn20Xml") String xml) {
}
