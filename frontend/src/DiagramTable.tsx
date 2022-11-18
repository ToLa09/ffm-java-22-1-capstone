import React, {useEffect, useState} from 'react';
import {BpmnDiagramModel} from "./model/BpmnDiagramModel";
import axios from "axios";
import DiagramLine from "./DiagramLine";
import './css/DiagramTable.css';

function DiagramTable() {
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
        <table className="diagramtable">
            <thead className="tableHead">
                <tr>
                    <th>Name</th>
                    <th>Businesskey</th>
                    <th>xmlFile</th>
                    <th>Comment</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                {
                    bpmnDiagrams.map(diagram => {
                        return <DiagramLine key={diagram.id} diagram={diagram} fetchDiagrams={fetchDiagrams}></DiagramLine>
                    })
                }
            </tbody>
        </table>
    );
}

export default DiagramTable;