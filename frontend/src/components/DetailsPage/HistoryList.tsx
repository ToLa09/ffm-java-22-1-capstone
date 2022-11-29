import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import {CardContent, Table, TableBody, TableCell, TableHead, TableRow, Typography} from "@mui/material";
import HistoryListRow from "./HistoryListRow";
import axios from "axios";

type HistoryListProps = {
    latestDiagram: BpmnDiagramModel
    setSnackbarOpen: Dispatch<SetStateAction<boolean>>
    setSnackbarMessage: Dispatch<SetStateAction<string>>
    fetchDiagrams: () => void
    setTab: Dispatch<SetStateAction<string>>
}

function HistoryList(props: HistoryListProps) {

    const [history, setHistory] = useState<BpmnDiagramModel[]>([])

    const fetchHistory = () => {
        axios.get("/api/bpmndiagrams/history/" + props.latestDiagram.businessKey)
            .then(response => response.data)
            .then(setHistory)
    }

    useEffect(fetchHistory, [props.latestDiagram.businessKey])

    return (
        <CardContent>
            <Typography variant="h5"
                        color="secondary">History</Typography>
            <Table size="small">
                <TableHead>
                    <TableRow>
                        <TableCell>Name</TableCell>
                        <TableCell align="center">Filename</TableCell>
                        <TableCell align="center">version</TableCell>
                        {props.latestDiagram.customDiagram &&
                            <TableCell align="right">Delete</TableCell>
                        }
                    </TableRow>
                </TableHead>
                <TableBody>
                    {history.map(diagram => <HistoryListRow
                        key={diagram.id}
                        diagram={diagram}
                        fetchHistory={fetchHistory}
                        fetchDiagrams={props.fetchDiagrams}
                        latestDiagram={props.latestDiagram}
                        setTab={props.setTab}
                    />)}
                </TableBody>
            </Table>
        </CardContent>
    );
}

export default HistoryList;