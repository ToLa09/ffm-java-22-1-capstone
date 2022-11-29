import React, {ChangeEvent, useEffect, useState} from 'react';
import {
    Button,
    Card,
    CardContent,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Grid,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    TextField,
    Typography
} from "@mui/material";
import axios from "axios";
import {CommentModel} from "../../model/CommentModel";
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";

type CommentListProps = {
    detailedDiagram: BpmnDiagramModel
}

function CommentList(props: CommentListProps) {

    const [openAddCommentDialog, setOpenAddCommentDialog] = useState<boolean>(false)
    const [commentList, setCommentList] = useState<CommentModel[]>([])
    const [newComment, setNewComment] = useState<CommentModel>({
        id: "",
        content: "",
        author: "",
        time: "",
        diagramId: props.detailedDiagram.id
    })

    const fetchComments = () => {
        axios.get("/api/comments/" + props.detailedDiagram.id)
            .then(response => response.data)
            .then(setCommentList)
    }

    useEffect(fetchComments, [props.detailedDiagram.id])

    const handleAddComment = () => {
        axios.post("/api/comments", newComment)
            .then(() => {
                setOpenAddCommentDialog(false)
                fetchComments()
            })
    }

    function handleChange(event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        setNewComment({
            ...newComment,
            [event.target.id]: event.target.value
        })
    }

    return (
        <Card>
            <CardContent>
                <Grid container>
                    <Grid item xs={10}><Typography variant="h5" color="secondary">Comments</Typography></Grid>
                    <Grid item><Button variant="outlined" color="secondary"
                                       onClick={() => setOpenAddCommentDialog(true)}>Add</Button></Grid>
                </Grid>
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            <TableCell>Comment</TableCell>
                            <TableCell align="center">Author</TableCell>
                            <TableCell align="center">Time</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {commentList.map(comment => {
                            return <TableRow key={comment.id}>
                                <TableCell>{comment.content}</TableCell>
                                <TableCell>{comment.author}</TableCell>
                                <TableCell>{comment.time}</TableCell>
                            </TableRow>
                        })
                        }
                    </TableBody>
                </Table>
            </CardContent>

            <Dialog open={openAddCommentDialog} onClose={() => setOpenAddCommentDialog(false)}>
                <DialogTitle>Add Comment</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Write your stuff here:
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="content"
                        label="Comment Text"
                        type="text"
                        fullWidth
                        variant="standard"
                        onChange={handleChange}
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="author"
                        label="Comment Author"
                        type="text"
                        fullWidth
                        variant="standard"
                        onChange={handleChange}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setOpenAddCommentDialog(false)}>Cancel</Button>
                    <Button onClick={handleAddComment}>Add</Button>
                </DialogActions>
            </Dialog>
        </Card>
    );
}

export default CommentList;