import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import {Card, CardContent, Table, TableBody, TableCell, TableHead, TableRow, Typography} from "@mui/material";
import HistoryListRow from "./HistoryListRow";
import axios from "axios";

type HistoryListProps = {
    latestDiagram: BpmnDiagramModel
    setSnackbarOpen: Dispatch<SetStateAction<boolean>>
    setSnackbarMessage: Dispatch<SetStateAction<string>>
    fetchDiagrams: () => void
}

function HistoryList(props: HistoryListProps) {

    const [history, setHistory] = useState<BpmnDiagramModel[]>([])

    const fetchHistory = () => {
        axios.get("/api/bpmndiagrams/" + props.latestDiagram.businessKey + "/history")
            .then(response => response.data)
            .then(setHistory)
            .catch(error => console.error("Error fetching History: " + error))
    }

    useEffect(fetchHistory, [props.latestDiagram.businessKey])

    return (
        <Card>
            <CardContent>
                <Typography variant="h5"
                            color="secondary">History</Typography>
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell align="center">Filename</TableCell>
                            <TableCell align="center">Version</TableCell>
                            <TableCell align="left">Comments</TableCell>
                            {props.latestDiagram.customDiagram &&
                                <TableCell align="right">Delete</TableCell>
                            }
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {history
                            .map(diagram => {
                                    return <HistoryListRow
                                        key={diagram.id}
                                        diagram={diagram}
                                        fetchHistory={fetchHistory}
                                        fetchDiagrams={props.fetchDiagrams}
                                        latestDiagram={props.latestDiagram}
                                    />
                                }
                            )}
                    </TableBody>
                </Table>
            </CardContent>
        </Card>
    );
}

export default HistoryList;