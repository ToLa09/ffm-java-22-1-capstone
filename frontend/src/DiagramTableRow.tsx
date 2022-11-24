import React, {Dispatch, SetStateAction} from 'react';
import {BpmnDiagramModel} from "./model/BpmnDiagramModel";
import './css/DiagramLine.css';
import TableCell from "@mui/material/TableCell";
import TableRow from "@mui/material/TableRow";
import {Button} from "@mui/material";

export type Props = {
    diagram: BpmnDiagramModel
    fetchDiagrams: () => void
    setValue: Dispatch<SetStateAction<string>>
    setDetailedDiagram: Dispatch<SetStateAction<BpmnDiagramModel>>
}

export default function DiagramTableRow(props: Props) {

    const handleDetails = () => {
        props.setValue("Details")
        props.setDetailedDiagram(props.diagram)
    }

    return (
        <>
            <TableRow key={props.diagram.id} >
                <TableCell component="td" scope="row">{props.diagram.name}</TableCell>
                <TableCell align="left">{props.diagram.filename}</TableCell>
                <TableCell align="center">{props.diagram.version}</TableCell>
                <TableCell align="left">{props.diagram.commentText}</TableCell>
                <TableCell align="center"><Button onClick={handleDetails} color="secondary">Details</Button></TableCell>
            </TableRow>
        </>
    );
}