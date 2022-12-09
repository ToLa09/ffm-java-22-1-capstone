import React, {useCallback, useEffect, useState} from 'react';
import {IconButton, TableCell, TableRow, Typography} from "@mui/material";
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import {useNavigate} from "react-router-dom";
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';

type CalledDiagramListRowProps = {
    calledDiagramId: string
    calledFromActivities: string []
    bpmnDiagrams: BpmnDiagramModel[]
}

function CalledDiagramListRow(props: CalledDiagramListRowProps) {
    const navigate = useNavigate()

    const [diagram, setDiagram] = useState<BpmnDiagramModel>({
        id: ""
        , name: "-"
        , businessKey: "-"
        , filename: "-"
        , version: 1
        , comments: []
        , calledDiagrams: []
        , template: false
        , customDiagram: true
    })

    const getDiagramFromList = useCallback(() => {
        props.bpmnDiagrams.forEach((diagram) => {
            if (diagram.id === props.calledDiagramId) {
                setDiagram(diagram)
            }
        })
    }, [props.bpmnDiagrams, props.calledDiagramId])

    useEffect(getDiagramFromList, [getDiagramFromList])

    const handleNavigate = useCallback(() => navigate("/" + props.calledDiagramId)
        , [navigate, props.calledDiagramId])

    return (
        <TableRow key={props.calledDiagramId}>
            <TableCell>{diagram.name}</TableCell>
            <TableCell align="center">{diagram.businessKey}</TableCell>
            <TableCell align="center">{diagram.version}</TableCell>
            <TableCell align="center">
                {props.calledFromActivities.map((activity) => {
                    return <Typography key={activity} variant="body2">{activity}</Typography>
                })}
            </TableCell>
            <TableCell align="center">
                <IconButton onClick={handleNavigate}>
                    <ArrowForwardIcon color="secondary"/>
                </IconButton>
            </TableCell>
        </TableRow>
    )
}

export default CalledDiagramListRow;