import React, {useState} from 'react';
import {BpmnDiagramModel} from "./BpmnDiagramModel";
import axios from "axios";

export type Props = {
    diagram: BpmnDiagramModel
    fetchDiagrams: () => void
}

function DiagramLine(props: Props) {

    const[editMode, setEditMode] = useState<boolean>(false)
    const [name,setName] = useState<string>(props.diagram.name)
    const [businessKey,setBusinessKey] = useState<string>(props.diagram.businessKey)
    const [xmlFile,setXmlFile] = useState<string>(props.diagram.xmlFile)
    const [comment,setComment] = useState<string>(props.diagram.comment)

    const handleUpdate = () => {
        axios.put("/api/bpmndiagrams/"+props.diagram.id, {
            "id": props.diagram.id,
            "name": name,
            "businessKey": businessKey,
            "xmlFile": xmlFile,
            "comment":comment
        })
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
            {editMode ?
                <>
                    <td><input placeholder={"name"} value={name} onChange={(event) => setName(event.target.value)}/></td>
                    <td><input placeholder={"businessKey"} value={businessKey} onChange={(event) => setBusinessKey(event.target.value)}/></td>
                    <td><input placeholder={"xmlFile"} value={xmlFile} onChange={(event) => setXmlFile(event.target.value)}/></td>
                    <td><input placeholder={"comment"} value={comment} onChange={(event) => setComment(event.target.value)}/></td>
                </>
                :
                <>
                    <td>{props.diagram.name}</td>
                    <td>{props.diagram.businessKey}</td>
                    <td>{props.diagram.xmlFile}</td>
                    <td>{props.diagram.comment}</td>
                </>
            }

            <td>
                {editMode ?
                    <button onClick={() => {
                                            setEditMode(false)
                                            handleUpdate()
                                            }} >Update
                    </button>
                    :
                    <button onClick={() => setEditMode(true)} >Edit</button>
                }
                <button onClick={() => handleDuplicate(props.diagram)}>Duplicate</button>
                <button onClick={() => handleDelete(props.diagram.id)}>Delete</button>
            </td>
        </tr>
    );
}

export default DiagramLine;