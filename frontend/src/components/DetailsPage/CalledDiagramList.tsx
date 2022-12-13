import React from 'react';
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import {Card, CardContent, Table, TableBody, TableCell, TableHead, TableRow, Typography} from "@mui/material";
import CalledDiagramListRow from "./CalledDiagramListRow";

type CalledDiagramListProps = {
    diagram: BpmnDiagramModel
    bpmnDiagrams: BpmnDiagramModel[]
}

function CalledDiagramList(props: CalledDiagramListProps) {

    return (
        <Card>
            <CardContent>
                <Typography variant="h5" color="secondary">Called Processes</Typography>
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell align="center">Business Key</TableCell>
                            <TableCell align="center">Version</TableCell>
                            <TableCell align="center">Called By Activities</TableCell>
                            <TableCell align="center">Go To Diagram</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {
                            props.diagram.calledDiagrams.map(({id, calledFromActivityIds}) => {
                                return <CalledDiagramListRow
                                    key={id}
                                    id={id}
                                    calledFromActivityIds={calledFromActivityIds}
                                    bpmnDiagrams={props.bpmnDiagrams}
                                />
                            })
                        }
                    </TableBody>
                </Table>
            </CardContent>
        </Card>
    );
}

export default CalledDiagramList;
