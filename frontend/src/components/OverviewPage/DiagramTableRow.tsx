import React, {Dispatch, SetStateAction, useCallback, useEffect, useState} from 'react';
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import TableCell from "@mui/material/TableCell";
import TableRow from "@mui/material/TableRow";
import {Button} from "@mui/material";
import CommentListSimple from "../CommentListSimple";
import {useNavigate} from "react-router-dom";
import axios from "axios";

export type Props = {
    diagram: BpmnDiagramModel
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

    const fetchCalledByDiagramsList = useCallback(() => {
        axios.get("/api/bpmndiagrams/" + props.diagram.id + "/calledby")
            .then(response => response.data)
            .catch(error => console.error(error))
            .then(setCalledByDiagrams)
    }, [props.diagram.id])

    useEffect(fetchCalledByDiagramsList, [fetchCalledByDiagramsList])


    return (
        <>
            <TableRow key={props.diagram.id}>
                <TableCell component="td" scope="row">{props.diagram.name}</TableCell>
                <TableCell align="left">{props.diagram.filename}</TableCell>
                <TableCell align="center">{props.diagram.version}</TableCell>
                <TableCell align="center">
                    <CommentListSimple diagram={props.diagram}/>
                </TableCell>
                <TableCell
                    align="center">{props.diagram.calledDiagrams !== null && props.diagram.calledDiagrams.length}</TableCell>
                <TableCell align="center">{calledByDiagrams !== null && calledByDiagrams.length}</TableCell>
                <TableCell align="center">{props.diagram.customDiagram ? "changeable" : "immutable"}</TableCell>
                <TableCell align="center"><Button onClick={handleDetails} color="secondary">Show
                    Details</Button></TableCell>
            </TableRow>
        </>
    );
}