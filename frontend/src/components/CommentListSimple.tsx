import React from 'react';
import {Typography} from "@mui/material";
import moment from "moment/moment";
import {BpmnDiagramModel} from "../model/BpmnDiagramModel";

type CommentListProps = {
    diagram: BpmnDiagramModel
}

function CommentListSimple(props: CommentListProps) {

    return (
        <>
            {[...props.diagram.comments]
                .sort((comment1, comment2) => {
                    if (comment1.time > comment2.time) {
                        return -1
                    } else return 0
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