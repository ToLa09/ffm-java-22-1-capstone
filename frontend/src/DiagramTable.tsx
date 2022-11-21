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
import {Snackbar} from "@mui/material";

function DiagramTable() {
    const [bpmnDiagrams, setBpmnDiagrams] = useState<BpmnDiagramModel[]>([])
    const [snackbarDeleteOpen, setSnackbarDeleteOpen] = useState<boolean>(false)
    const [snackbarDuplicateOpen, setSnackbarDuplicateOpen] = useState<boolean>(false)

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
        <>
            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} size="small" aria-label="BPMN-Diagram-table" className="diagramtable">
                    <TableHead>
                        <TableRow>
                            <TableCell sx={{fontWeight: "bold"}} >Name</TableCell>
                            <TableCell sx={{fontWeight: "bold"}} align="right">Key</TableCell>
                            <TableCell sx={{fontWeight: "bold"}} align="right">XML-File</TableCell>
                            <TableCell sx={{fontWeight: "bold"}} align="right">Comment</TableCell>
                            <TableCell sx={{fontWeight: "bold", width: 120}} align="center">Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {bpmnDiagrams.map(diagram => (
                            <DiagramTableRow
                                key={diagram.id}
                                diagram={diagram}
                                fetchDiagrams={fetchDiagrams}
                                setSnackbarDeleteOpen={setSnackbarDeleteOpen}
                                setSnackbarDuplicateOpen={setSnackbarDuplicateOpen}></DiagramTableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <Snackbar
                open={snackbarDeleteOpen}
                autoHideDuration={3000}
                message="Diagram deleted!"
                onClose={() => setSnackbarDeleteOpen(false)}
            />
            <Snackbar
                open={snackbarDuplicateOpen}
                autoHideDuration={3000}
                message="Diagram duplicated!"
                onClose={() => setSnackbarDuplicateOpen(false)}
            />
        </>
    );
}

export default DiagramTable;
