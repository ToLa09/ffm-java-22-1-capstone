import React, {useEffect, useState} from 'react';
import {BpmnDiagramModel} from "./BpmnDiagramModel";
import axios from "axios";

function DiagramBoard() {
    const [bpmnDiagrams, setBpmnDiagrams] = useState<BpmnDiagramModel[]>([])

    const fetchDiagrams = () => {
        axios.get("/api/bpmndiagrams")
            .then(response => response.data)
            .catch(error => console.error("GET-Error: " + error))
            .then(setBpmnDiagrams)
    }

    useEffect(() => {
        fetchDiagrams()
    },[])

    return (
        <ul>
            {
                bpmnDiagrams.map(diagram => {
                    return <li key={diagram.id}>{diagram.name}, {diagram.businessKey}, {diagram.xmlFile}, {diagram.comment}</li>
                })
            }
        </ul>
    );
}

export default DiagramBoard;