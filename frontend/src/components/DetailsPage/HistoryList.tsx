import React, {Dispatch, SetStateAction} from 'react';
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import {Table, TableBody, TableCell, TableHead, TableRow} from "@mui/material";
import HistoryListRow from "./HistoryListRow";

type HistoryListProps = {
    history: BpmnDiagramModel[]
    latestDiagram: BpmnDiagramModel
    customDiagram: boolean
    setSnackbarOpen: Dispatch<SetStateAction<boolean>>
    setSnackbarMessage: Dispatch<SetStateAction<string>>
    fetchHistory: () => void
    fetchDiagrams: () => void
    setTab: Dispatch<SetStateAction<string>>
}

function HistoryList(props: HistoryListProps) {


    return (
        <Table size="small">
            <TableHead>
                <TableRow>
                    <TableCell sx={{fontWeight: "bold"}}>Name</TableCell>
                    <TableCell sx={{fontWeight: "bold"}} align="center">Filename</TableCell>
                    <TableCell sx={{fontWeight: "bold"}} align="center">version</TableCell>
                    {props.customDiagram &&
                        <TableCell sx={{fontWeight: "bold"}} align="right">Delete</TableCell>
                    }
                </TableRow>
            </TableHead>
            <TableBody>
                {props.history.map(diagram => <HistoryListRow
                    key={diagram.id}
                    diagram={diagram}
                    fetchHistory={props.fetchHistory}
                    fetchDiagrams={props.fetchDiagrams}
                    latestDiagram={props.latestDiagram}
                    setTab={props.setTab}
                />)}
            </TableBody>
        </Table>
    );
}

export default HistoryList;