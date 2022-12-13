import React, {useCallback, useEffect, useState} from 'react';
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import {
    Card,
    CardContent,
    IconButton,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    Typography
} from "@mui/material";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import {useNavigate} from "react-router-dom";

type CalledByDiagramListProps = {
    detailedDiagramId: string
    bpmnDiagrams: BpmnDiagramModel[]
}

function CalledByDiagramList(props: CalledByDiagramListProps) {
    const navigate = useNavigate()

    const [calledByDiagrams, setCalledByDiagrams] = useState<BpmnDiagramModel[]>([])

    const getCalledByDiagramList = useCallback(() => {
        let calledByDiagrams: BpmnDiagramModel[] = []
        props.bpmnDiagrams.forEach((latestDiagram) => {
            latestDiagram.calledDiagrams.forEach((calledDiagram) => {
                if (calledDiagram.id === props.detailedDiagramId) {
                    calledByDiagrams.push(latestDiagram)
                }
            })
        })
        setCalledByDiagrams(calledByDiagrams)
    }, [props.bpmnDiagrams, props.detailedDiagramId])

    useEffect(getCalledByDiagramList, [getCalledByDiagramList])

    return (
        <>
            {calledByDiagrams.length !== 0 &&
                <Card>
                    <CardContent>
                        <Typography variant="h5" color="secondary">Called by Processes</Typography>
                        <Table size="small">
                            <TableHead>
                                <TableRow>
                                    <TableCell>Name</TableCell>
                                    <TableCell align="center">Business Key</TableCell>
                                    <TableCell align="center">ID</TableCell>
                                    <TableCell align="center">Version</TableCell>
                                    <TableCell align="center">Go To Diagram</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {
                                    calledByDiagrams.map(diagram => {
                                        return <TableRow key={diagram.id}>
                                            <TableCell>{diagram.name}</TableCell>
                                            <TableCell align="center">{diagram.businessKey}</TableCell>
                                            <TableCell align="center">{diagram.id}</TableCell>
                                            <TableCell align="center">{diagram.version}</TableCell>
                                            <TableCell align="center">
                                                <IconButton onClick={() => navigate("/" + diagram.id)}>
                                                    <ArrowForwardIcon color="secondary"/>
                                                </IconButton>
                                            </TableCell>
                                        </TableRow>
                                    })
                                }
                            </TableBody>
                        </Table>
                    </CardContent>
                </Card>
            }
        </>
    );
}

export default CalledByDiagramList;
