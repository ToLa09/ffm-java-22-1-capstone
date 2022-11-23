import React, {Dispatch, SetStateAction, useState} from 'react';
import {BpmnDiagramModel} from "./model/BpmnDiagramModel";
import {Box, Button, Card, CardContent, Container, Grid, Snackbar, Typography} from "@mui/material";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";

type DiagramDetailsProps = {
    setTab: Dispatch<SetStateAction<string>>
    detailedDiagram: BpmnDiagramModel
    fetchDiagrams: () => void
}

function DiagramDetails(props: DiagramDetailsProps) {

    const [snackbarDeleteOpen, setSnackbarDeleteOpen] = useState<boolean>(false)
    const [snackbarDuplicateOpen, setSnackbarDuplicateOpen] = useState<boolean>(false)
    const [snackbarUpdateOpen, setSnackbarUpdateOpen] = useState<boolean>(false)


    // const [name,setName] = useState<string>(bpmnDiagram.name)
    // const [businessKey,setBusinessKey] = useState<string>(props.diagram.businessKey)
    // const [filename,setFilename] = useState<string>(props.diagram.filename)
    // const [comment,setComment] = useState<string>(props.diagram.commentText)
    //
    // const handleUpdate = () => {
    //     axios.put("/api/bpmndiagrams/"+props.diagram.id, {
    //         "id": props.diagram.id,
    //         "name": name,
    //         "businessKey": businessKey,
    //         "xmlFile": filename,
    //         "comment":comment
    //     })
    //         .then(() => {
    //             props.fetchDiagrams()
    //             if(name!==props.diagram.name || businessKey!==props.diagram.businessKey || filename!==props.diagram.filename || comment!==props.diagram.commentText){
    //                 props.setSnackbarUpdateOpen(true)
    //             }
    //         })
    //         .catch(error => console.error(error))
    // }
    //
    // const handleDuplicate = (diagram: BpmnDiagramModel) => {
    //     axios.post("/api/bpmndiagrams", {
    //         "name": diagram.name,
    //         "businessKey": diagram.businessKey,
    //         "xmlFile": diagram.filename,
    //         "comment": "duplicated"
    //     })
    //         .then(() => {
    //             props.fetchDiagrams()
    //             props.setSnackbarDuplicateOpen(true)
    //         })
    //         .catch(error => console.error(error))
    // }
    //
    // const handleDelete = (id: string) => {
    //     axios.delete("/api/bpmndiagrams/"+id)
    //         .then(() => {
    //             props.fetchDiagrams()
    //             props.setSnackbarDeleteOpen(true)
    //         })
    //         .catch(error => console.error(error))
    // }


    return (
        <>
            <Grid container spacing={2}>
                <Grid item xs={12}><Button variant="outlined" color="secondary" onClick={() => props.setTab("Overview")}>Back</Button></Grid>
                <Grid item xs={6}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5" color="secondary">Properties</Typography>
                            <Typography>Name: {props.detailedDiagram.name}</Typography>
                            <Typography>Business Key: {props.detailedDiagram.businessKey}</Typography>
                            <Typography>Filename: {props.detailedDiagram.filename}</Typography>
                            <Typography>Latest Version: {props.detailedDiagram.version}</Typography>
                        </CardContent>
                    </Card>
                </Grid>
                <Grid item xs={6}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5" color="secondary">Comments</Typography>
                        </CardContent>
                    </Card>
                </Grid>
                <Grid item xs={6}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5" color="secondary">History</Typography>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>
            <Snackbar
                open={snackbarDeleteOpen}
                autoHideDuration={3000}
                message="Diagram deleted!"
                onClose={() => setSnackbarDeleteOpen(false)}
                action={<IconButton onClick={() => setSnackbarDeleteOpen(false)}><CloseIcon/></IconButton>}
            />
            <Snackbar
                open={snackbarDuplicateOpen}
                autoHideDuration={3000}
                message="Diagram duplicated!"
                onClose={() => setSnackbarDuplicateOpen(false)}
                action={<IconButton onClick={() => setSnackbarDuplicateOpen(false)}><CloseIcon/></IconButton>}
            />
            <Snackbar
                open={snackbarUpdateOpen}
                autoHideDuration={3000}
                message="Diagram updated!"
                onClose={() => setSnackbarUpdateOpen(false)}
                action={<IconButton onClick={() => setSnackbarUpdateOpen(false)}><CloseIcon/></IconButton>}
            />
        </>
    );
}

export default DiagramDetails;