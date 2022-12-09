import React, {Dispatch, SetStateAction, useCallback, useEffect, useState} from 'react';
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import TableCell from "@mui/material/TableCell";
import TableRow from "@mui/material/TableRow";
import {Button} from "@mui/material";
import CommentListSimple from "../CommentListSimple";
import {useNavigate} from "react-router-dom";

export type Props = {
    diagram: BpmnDiagramModel
    bpmnDiagrams: BpmnDiagramModel[]
    fetchDiagrams: () => void
    setDetailedDiagramId: Dispatch<SetStateAction<string>>
}

export default function DiagramTableRow(props: Props) {
    const navigate = useNavigate()

    const [calledByDiagrams, setCalledByDiagrams] = useState<BpmnDiagramModel[]>([])

    const handleDetails = () => {
        props.setDetailedDiagramId(props.diagram.id)
        navigate("/" + props.diagram.id)
    }

    const getCalledByDiagramList = useCallback(() => {
        let calledByDiagrams: BpmnDiagramModel[] = []
        props.bpmnDiagrams.forEach((latestDiagram) => {
            latestDiagram.calledDiagrams.forEach((calledDiagram) => {
                if (calledDiagram.calledDiagramId === props.diagram.id) {
                    calledByDiagrams.push(latestDiagram)
                }
            })
        })
        setCalledByDiagrams(calledByDiagrams)
    }, [props.bpmnDiagrams, props.diagram.id])

    useEffect(getCalledByDiagramList, [getCalledByDiagramList])

    return (
        <>
            <TableRow key={props.diagram.id}>
                <TableCell component="td" scope="row">{props.diagram.name}</TableCell>
                <TableCell align="left">{props.diagram.filename}</TableCell>
                <TableCell align="center">{props.diagram.version}</TableCell>
                <TableCell align="center">
                    <CommentListSimple diagram={props.diagram}/>
                </TableCell>
                <TableCell align="center">
                    {props.diagram.calledDiagrams !== null && props.diagram.calledDiagrams.length}<> /</>
                    {calledByDiagrams !== null && calledByDiagrams.length}
                </TableCell>
                <TableCell align="center">{props.diagram.customDiagram ? "changeable" : "immutable"}</TableCell>
                <TableCell align="center"><Button onClick={handleDetails} color="secondary">Show
                    Details</Button></TableCell>
            </TableRow>
        </>
    );
}