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
                <Typography variant="h5" color="secondary">Called Bpmn Diagrams</Typography>
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell align="center">Business Key</TableCell>
                            <TableCell align="center">Version</TableCell>
                            <TableCell align="center">Called By Activities</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {
                            props.diagram.calledDiagrams.map(({calledDiagramId, calledFromActivities}) => {
                                return <CalledDiagramListRow
                                    key={calledDiagramId}
                                    calledDiagramId={calledDiagramId}
                                    calledFromActivities={calledFromActivities}
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