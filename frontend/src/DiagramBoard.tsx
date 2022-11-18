import React, {useEffect, useState} from 'react';
import {BpmnDiagramModel} from "./BpmnDiagramModel";
import axios from "axios";
import DiagramLine from "./DiagramLine";

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
                <th>Action</th>
            </tr>
            {
                bpmnDiagrams.map(diagram => {
                    return <DiagramLine diagram={diagram} fetchDiagrams={fetchDiagrams}></DiagramLine>
                })
            }
        </table>
    );
}

export default DiagramBoard;