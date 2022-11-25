import React from 'react';
import {BpmnDiagramModel} from "./model/BpmnDiagramModel";
import {Table, TableBody, TableCell, TableHead, TableRow} from "@mui/material";

type HistoryListProps = {
    history: BpmnDiagramModel[]
}

function HistoryList(props: HistoryListProps) {
    return (
        <Table>
            <TableHead>
                <TableRow>
                    <TableCell sx={{fontWeight: "bold"}}>Name</TableCell>
                    <TableCell sx={{fontWeight: "bold"}} align="center">Filename</TableCell>
                    <TableCell sx={{fontWeight: "bold"}} align="center">version</TableCell>
                </TableRow>
            </TableHead>
            <TableBody>
                {
                    props.history.map(diagram => {
                        return (
                            <TableRow>
                                <TableCell>{diagram.name}</TableCell>
                                <TableCell align="center">{diagram.businessKey}</TableCell>
                                <TableCell align="center">{diagram.version}</TableCell>
                            </TableRow>
                        )
                    })
                }
            </TableBody>
        </Table>
    );
}

export default HistoryList;