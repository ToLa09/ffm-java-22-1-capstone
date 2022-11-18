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
        <table>
            <tr>
                <th>Name</th>
                <th>Businesskey</th>
                <th>xmlFile</th>
                <th>Comment</th>
            </tr>
            {
                bpmnDiagrams.map(diagram => {
                    return <tr key={diagram.id}>
                        <td>{diagram.name}</td>
                        <td>{diagram.businessKey}</td>
                        <td>{diagram.xmlFile}</td>
                        <td>{diagram.comment}</td>
                    </tr>
                })
            }
        </table>
    );
}

export default DiagramBoard;