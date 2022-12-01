import React, {Dispatch, SetStateAction} from 'react';
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import TableCell from "@mui/material/TableCell";
import TableRow from "@mui/material/TableRow";
import {Button} from "@mui/material";
import CommentListSimple from "../CommentListSimple";

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
            <TableRow key={props.diagram.id}>
                <TableCell component="td" scope="row">{props.diagram.name}</TableCell>
                <TableCell align="left">{props.diagram.filename}</TableCell>
                <TableCell align="center">{props.diagram.version}</TableCell>
                <TableCell align="center">
                    <CommentListSimple diagram={props.diagram}/>
                </TableCell>
                <TableCell align="center">{props.diagram.customDiagram ? "changeable" : "immutable"}</TableCell>
                <TableCell align="center"><Button onClick={handleDetails} color="secondary">Show
                    Details</Button></TableCell>
            </TableRow>
        </>
    );
}