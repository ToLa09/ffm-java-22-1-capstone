import React, {useEffect, useState} from 'react';
import {BpmnDiagramModel} from "./model/BpmnDiagramModel";
import axios from "axios";
import DiagramTableRow from "./DiagramTableRow";
import './css/DiagramTable.css';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

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
        <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="BPMN-Diagram-table">
                <TableHead>
                    <TableRow>
                        <TableCell >Name</TableCell>
                        <TableCell align="right">Key</TableCell>
                        <TableCell align="right">XML-File</TableCell>
                        <TableCell align="right">Comment</TableCell>
                        <TableCell sx={{width: 120}} align="center">Actions</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {bpmnDiagrams.map(diagram => (
                        <DiagramTableRow diagram={diagram} fetchDiagrams={fetchDiagrams}></DiagramTableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default DiagramTable;
