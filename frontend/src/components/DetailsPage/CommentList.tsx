import React, {ChangeEvent, useState} from 'react';
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
    IconButton,
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
import moment from "moment";
import DeleteIcon from "@mui/icons-material/Delete";

type CommentListProps = {
    detailedDiagram: BpmnDiagramModel
    fetchDiagrams: () => void
}

function CommentList(props: CommentListProps) {

    const [openAddCommentDialog, setOpenAddCommentDialog] = useState<boolean>(false)
    const [commentList, setCommentList] = useState<CommentModel[]>(props.detailedDiagram.comments)
    const [newComment, setNewComment] = useState<CommentModel>({
        id: "",
        content: "",
        author: "",
        time: "",
    })

    const fetchComments = () => {
        axios.get("/api/bpmndiagrams/" + props.detailedDiagram.id + "/comments")
            .then(response => response.data)
            .then(setCommentList)
            .catch(error => console.error("Error fetching comments: " + error))
    }

    const handleAddComment = () => {
        axios.post("/api/bpmndiagrams/" + props.detailedDiagram.id + "/comments", newComment)
            .then(() => {
                setOpenAddCommentDialog(false)
                fetchComments()
                props.fetchDiagrams()
            })
            .catch(error => console.error("Error adding comment: " + error))
    }

    function handleChange(event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        setNewComment({
            ...newComment,
            [event.target.id]: event.target.value
        })
    }

    const handleDelete = (id: string) => {
        axios.delete("/api/bpmndiagrams/" + props.detailedDiagram.id + "/comments/" + id)
            .then(() => {
                fetchComments()
                props.fetchDiagrams()
            })
            .catch(error => console.error("Error deleting comment: " + error))
    }

    return (
        <Card>
            <CardContent>
                <Grid container>
                    <Grid item xs><Typography variant="h5" color="secondary">Comments</Typography></Grid>
                    <Grid item xs="auto"><Button variant="outlined" color="secondary"
                                                 onClick={() => setOpenAddCommentDialog(true)}>Create New
                        Comment</Button></Grid>
                </Grid>
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            <TableCell>Comment</TableCell>
                            <TableCell>Author</TableCell>
                            <TableCell>Time</TableCell>
                            <TableCell>Delete</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {[...commentList]
                            .sort((comment1, comment2) => {
                                if (comment1.time > comment2.time) {
                                    return -1
                                } else return 1
                            })
                            .map(comment => {
                                return <TableRow key={comment.id}>
                                    <TableCell>{comment.content}</TableCell>
                                    <TableCell>{comment.author}</TableCell>
                                    <TableCell>{moment(comment.time).locale("de").format("DD.MM.YYYY, HH:mm")}</TableCell>
                                    <TableCell><IconButton size="small" color="error"
                                                           onClick={() => handleDelete(comment.id)}><DeleteIcon/></IconButton></TableCell>
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