import React from 'react';
import {BpmnDiagramModel} from "./BpmnDiagramModel";
import axios from "axios";

export type Props = {
    diagram: BpmnDiagramModel
    fetchDiagrams: () => void
}

function DiagramLine(props: Props) {

    const handleUpdate = (id: string) => {
        axios.put("/api/bpmndiagrams/"+id)
            .then(props.fetchDiagrams)
            .catch(error => console.error(error))
    }

    const handleDuplicate = (diagram: BpmnDiagramModel) => {
        axios.post("/api/bpmndiagrams", {
                "name": diagram.name,
                "businessKey": diagram.businessKey,
                "xmlFile": diagram.xmlFile,
                "comment": "duplicated"
            })
            .then(props.fetchDiagrams)
            .catch(error => console.error(error))
    }

    const handleDelete = (id: string) => {
        axios.delete("/api/bpmndiagrams/"+id)
            .then(props.fetchDiagrams)
            .catch(error => console.error(error))
    }

    return (
        <tr key={props.diagram.id}>
            <td>{props.diagram.name}</td>
            <td>{props.diagram.businessKey}</td>
            <td>{props.diagram.xmlFile}</td>
            <td>{props.diagram.comment}</td>
            <td>
                <button onClick={() => handleUpdate(props.diagram.id)}>Edit</button>
                <button onClick={() => handleDuplicate(props.diagram)}>Duplicate</button>
                <button onClick={() => handleDelete(props.diagram.id)}>Delete</button>
            </td>
        </tr>
    );
}

export default DiagramLine;