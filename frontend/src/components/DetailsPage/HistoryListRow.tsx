import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import {IconButton, TableCell, TableRow, Typography} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import axios from "axios";
import {CommentModel} from "../../model/CommentModel";

type HistoryListRowProps = {
    diagram: BpmnDiagramModel
    latestDiagram: BpmnDiagramModel
    fetchHistory: () => void
    fetchDiagrams: () => void
    setTab: Dispatch<SetStateAction<string>>
}

function HistoryListRow(props: HistoryListRowProps) {

    const [commentList, setCommentList] = useState<CommentModel[]>([])

    const handleDelete = () => {
        axios.delete("/api/bpmndiagrams/" + props.diagram.id)
            .then(() => {
                if (props.diagram.id === props.latestDiagram.id) {
                    props.fetchDiagrams()
                    props.setTab("Overview")
                } else props.fetchHistory()
            })
    }

    const fetchComments = () => {
        axios.get("/api/comments/" + props.diagram.id)
            .then(response => response.data)
            .then(setCommentList)
    }

    useEffect(fetchComments, [props.diagram.id])

    return (
        <TableRow key={props.diagram.businessKey}>
            <TableCell>{props.diagram.name}</TableCell>
            <TableCell align="center">{props.diagram.businessKey}</TableCell>
            <TableCell align="center">{props.diagram.version}</TableCell>
            <TableCell align="center">
                {commentList.map(comment => {
                    return <Typography variant="body1">{comment.author}: {comment.content}</Typography>
                })
                }
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