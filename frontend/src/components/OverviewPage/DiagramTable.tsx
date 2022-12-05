import React, {Dispatch, SetStateAction, useState} from 'react';
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import DiagramTableRow from "./DiagramTableRow";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import SyncIcon from '@mui/icons-material/Sync';
import Button from "@mui/material/Button";
import axios from "axios";
import CloseIcon from "@mui/icons-material/Close";
import {Box, Snackbar, Typography} from "@mui/material";
import IconButton from "@mui/material/IconButton";

type DiagramTableProps = {
    setTab: Dispatch<SetStateAction<string>>
    setDetailedDiagram: Dispatch<SetStateAction<BpmnDiagramModel>>
    bpmnDiagrams: BpmnDiagramModel[]
    fetchDiagrams: () => void
}

function DiagramTable(props: DiagramTableProps) {
    const [snackbarRefreshOpen, setSnackbarRefreshOpen] = useState<boolean>(false)

    const fetchCamundaDiagrams = () => {
        axios.post("/api/camundaprocesses")
            .then(response => {
                if(response.status === 204){
                    setSnackbarRefreshOpen(true)
                }
                props.fetchDiagrams()
            })
            .catch(error => console.error(error))
    }

    return (
        <>
            <Box mb={3}>
                <Button onClick={fetchCamundaDiagrams} variant="outlined" color="secondary">
                    <SyncIcon/><Typography>Fetch Diagrams from Camunda</Typography>
                </Button>
            </Box>

            <TableContainer component={Paper}>
                <Table sx={{minWidth: 650}} size="small" aria-label="BPMN-Diagram-table" className="diagramtable">
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell align="left">Filename</TableCell>
                            <TableCell align="center">latest Version</TableCell>
                            <TableCell align="center">latest Comment</TableCell>
                            <TableCell align="center">Changeability</TableCell>
                            <TableCell></TableCell>
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
