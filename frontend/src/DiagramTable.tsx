import React, {Dispatch, SetStateAction, useState} from 'react';
import {BpmnDiagramModel} from "./model/BpmnDiagramModel";
import DiagramTableRow from "./DiagramTableRow";
import './css/DiagramTable.css';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import SyncIcon from '@mui/icons-material/Sync';
import IconButton from "@mui/material/IconButton";
import axios from "axios";
import CloseIcon from "@mui/icons-material/Close";
import {Snackbar} from "@mui/material";

type DiagramTableProps = {
    setTab: Dispatch<SetStateAction<string>>
    setDetailedDiagram: Dispatch<SetStateAction<BpmnDiagramModel>>
    bpmnDiagrams: BpmnDiagramModel[]
    fetchDiagrams: () => void
}

function DiagramTable(props: DiagramTableProps) {
    const[snackbarRefreshOpen, setSnackbarRefreshOpen] = useState<boolean>(false)

    const fetchCamundaDiagrams = () => {
        axios.get("/api/camundaprocesses")
            .then(response => {
                if(response.status === 204){
                    setSnackbarRefreshOpen(true)
                }
            })
            .catch(error => console.error(error))
    }


    return (
        <>
            <IconButton onClick={fetchCamundaDiagrams}>
                <SyncIcon/>
            </IconButton>
            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} size="small" aria-label="BPMN-Diagram-table" className="diagramtable">
                    <TableHead>
                        <TableRow>
                            <TableCell sx={{fontWeight: "bold"}} >Name</TableCell>
                            <TableCell sx={{fontWeight: "bold"}} align="left">Filename</TableCell>
                            <TableCell sx={{fontWeight: "bold"}} align="center">latest version</TableCell>
                            <TableCell sx={{fontWeight: "bold"}} align="center">Custom Diagram (editable)</TableCell>
                            <TableCell sx={{fontWeight: "bold", width: 120}} align="center">Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {props.bpmnDiagrams.map(diagram => (
                            <DiagramTableRow
                                key={diagram.id}
                                diagram={diagram}
                                fetchDiagrams={props.fetchDiagrams}
                                setDetailedDiagram={props.setDetailedDiagram}
                                setValue={props.setTab}
                            ></DiagramTableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <Snackbar
                open={snackbarRefreshOpen}
                autoHideDuration={3000}
                message="Updated Diagramlist (Fetched Data from Camunda Engine)"
                onClose={() => setSnackbarRefreshOpen(false)}
                action={<IconButton onClick={() => setSnackbarRefreshOpen(false)}><CloseIcon/></IconButton>}
            />
        </>
    );
}

export default DiagramTable;
