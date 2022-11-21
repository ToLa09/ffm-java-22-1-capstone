import React, {useEffect, useState} from 'react';
import {BpmnDiagramModel} from "./model/BpmnDiagramModel";
import axios from "axios";
import DiagramTableLine from "./DiagramTableLine";
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
                    <td>Name</td>
                    <td>Businesskey</td>
                    <td>xmlFile</td>
                    <td>Comment</td>
                    <td>Action</td>
                </tr>
            </thead>
            <tbody className="tableBody">
                {
                    bpmnDiagrams.map(diagram => {
                        return <DiagramTableLine key={diagram.id} diagram={diagram} fetchDiagrams={fetchDiagrams}></DiagramTableLine>
                    })
                }
            </tbody>
        </table>
    );
}

export default DiagramTable;