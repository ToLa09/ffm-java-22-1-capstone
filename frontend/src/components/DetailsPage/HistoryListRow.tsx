import React, {Dispatch, SetStateAction} from 'react';
import {IconButton, TableCell, TableRow} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import axios from "axios";
import CommentListSimple from "../CommentListSimple";

type HistoryListRowProps = {
    diagram: BpmnDiagramModel
    latestDiagram: BpmnDiagramModel
    fetchHistory: () => void
    fetchDiagrams: () => void
    setTab: Dispatch<SetStateAction<string>>
}

function HistoryListRow(props: HistoryListRowProps) {

    const handleDelete = () => {
        axios.delete("/api/bpmndiagrams/" + props.diagram.id)
            .then(() => {
                if (props.diagram.id === props.latestDiagram.id) {
                    props.fetchDiagrams()
                    props.setTab("Overview")
                } else props.fetchHistory()
            })
    }

    return (
        <TableRow key={props.diagram.businessKey}>
            <TableCell>{props.diagram.name}</TableCell>
            <TableCell align="center">{props.diagram.businessKey}</TableCell>
            <TableCell align="center">{props.diagram.version}</TableCell>
            <TableCell align="left">
                <CommentListSimple diagram={props.diagram}/>
            </TableCell>
            {props.diagram.customDiagram &&
                <TableCell align="right">
                    <IconButton color="error" onClick={handleDelete}><DeleteIcon/></IconButton>
                </TableCell>
            }
        </TableRow>
    );
}

export default HistoryListRow;