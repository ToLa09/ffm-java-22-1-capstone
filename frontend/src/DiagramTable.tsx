import React, {Dispatch, SetStateAction} from 'react';
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

type DiagramTableProps = {
    setTab: Dispatch<SetStateAction<string>>
    setDetailedDiagram: Dispatch<SetStateAction<BpmnDiagramModel>>
    bpmnDiagrams: BpmnDiagramModel[]
    fetchDiagrams: () => void
}

function DiagramTable(props: DiagramTableProps) {




    return (
        <>
            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} size="small" aria-label="BPMN-Diagram-table" className="diagramtable">
                    <TableHead>
                        <TableRow>
                            <TableCell sx={{fontWeight: "bold"}} >Name</TableCell>
                            <TableCell sx={{fontWeight: "bold"}} align="left">Filename</TableCell>
                            <TableCell sx={{fontWeight: "bold"}} align="center">latest version</TableCell>
                            <TableCell sx={{fontWeight: "bold"}} align="left">Comment</TableCell>
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
        </>
    );
}

export default DiagramTable;
