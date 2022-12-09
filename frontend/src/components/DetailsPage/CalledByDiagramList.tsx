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
import BlockIcon from '@mui/icons-material/Block';
import {useNavigate} from "react-router-dom";
import axios from "axios";

type CalledByDiagramListProps = {
    detailedDiagramId: string
    bpmnDiagrams: BpmnDiagramModel[]
}

function CalledByDiagramList(props: CalledByDiagramListProps) {
    const navigate = useNavigate()

    const [calledByDiagrams, setCalledByDiagrams] = useState<BpmnDiagramModel[]>([])

    const fetchCalledByDiagramsList = useCallback(() => {
        axios.get("/api/bpmndiagrams/" + props.detailedDiagramId + "/calledby")
            .then(response => response.data)
            .catch(error => console.error(error))
            .then(setCalledByDiagrams)
    }, [props.detailedDiagramId])

    useEffect(fetchCalledByDiagramsList, [fetchCalledByDiagramsList])

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
                                        let latestVersion: boolean = false
                                        props.bpmnDiagrams.forEach(latestdiagram => {
                                            if (latestdiagram.id === diagram.id) {
                                                latestVersion = true;
                                            }
                                        })
                                        return <TableRow key={diagram.id}>
                                            <TableCell>{diagram.name}</TableCell>
                                            <TableCell align="center">{diagram.businessKey}</TableCell>
                                            <TableCell align="center">{diagram.id}</TableCell>
                                            <TableCell align="center">{diagram.version}</TableCell>
                                            {latestVersion ?
                                                <TableCell align="center">
                                                    <IconButton onClick={() => navigate("/" + diagram.id)}>
                                                        <ArrowForwardIcon color="secondary"/>
                                                    </IconButton>
                                                </TableCell>
                                                :
                                                <TableCell align="center">
                                                    <BlockIcon color="disabled"></BlockIcon>
                                                </TableCell>
                                            }
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