import React, {useEffect, useState} from 'react';
import {Typography} from "@mui/material";
import moment from "moment/moment";
import axios from "axios";
import {CommentModel} from "../model/CommentModel";
import {BpmnDiagramModel} from "../model/BpmnDiagramModel";

type CommentListProps = {
    diagram: BpmnDiagramModel
}

function CommentListSimple(props: CommentListProps) {

    const [commentList, setCommentList] = useState<CommentModel[]>([])

    const fetchComments = () => {
        axios.get("/api/comments/" + props.diagram.id)
            .then(response => response.data)
            .then(setCommentList)
    }

    useEffect(fetchComments, [props.diagram.id])

    return (
        <>
            {commentList
                .sort((comment1, comment2) => {
                    if (comment1.time > comment2.time) {
                        return -1
                    } else return 1
                })
                .map(comment => {
                    return <Typography key={comment.id}
                                       variant="body2">{moment(comment.time).format("DD.MM.YYYY HH:MM")} from {comment.author}: {comment.content}</Typography>
                })
            }
        </>
    );
}

export default CommentListSimple;