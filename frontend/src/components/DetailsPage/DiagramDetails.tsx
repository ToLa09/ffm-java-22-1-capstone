import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import {Box, Button, Grid, Snackbar,} from "@mui/material";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";
import HistoryList from "./HistoryList";
import CommentList from "./CommentList";
import PropertiesList from "./PropertiesList";
import BpmnViewer from "./BpmnViewer";
import {useNavigate, useParams} from "react-router-dom";

type DiagramDetailsProps = {
    detailedDiagram: BpmnDiagramModel
    setDetailedDiagram: Dispatch<SetStateAction<BpmnDiagramModel>>
    fetchDiagrams: () => void
    bpmnDiagrams: BpmnDiagramModel[]
}

function DiagramDetails(props: DiagramDetailsProps) {
    const {id} = useParams()
    const navigate = useNavigate()

    const [snackbarOpen, setSnackbarOpen] = useState<boolean>(false)
    const [snackbarMessage, setSnackbarMessage] = useState<string>("")

    useEffect(() => {
        props.bpmnDiagrams.forEach(diagram => {
            if (id === diagram.id) {
                props.setDetailedDiagram(diagram)
            }
        })
    }, [id, props])


    return (
        <Box m={5}>
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <Button
                        variant="outlined"
                        color="secondary"
                        onClick={() => navigate("/")}
                    >
                        Go back to Overview
                    </Button>
                </Grid>
                <Grid item xs={5}>
                    <PropertiesList
                        detailedDiagram={props.detailedDiagram}
                        fetchDiagrams={props.fetchDiagrams}
                        setSnackbarOpen={setSnackbarOpen}
                        setSnackbarMessage={setSnackbarMessage}
                        setDetailedDiagram={props.setDetailedDiagram}
                    />
                </Grid>
                <Grid item xs>
                    <CommentList
                        detailedDiagram={props.detailedDiagram}
                        fetchDiagrams={props.fetchDiagrams}
                    />
                </Grid>
                <Grid item xs={12}>
                    <HistoryList
                        latestDiagram={props.detailedDiagram}
                        setSnackbarOpen={setSnackbarOpen}
                        setSnackbarMessage={setSnackbarMessage}
                        fetchDiagrams={props.fetchDiagrams}
                    />
                </Grid>
                <Grid item xs={12}>
                    <BpmnViewer diagram={props.detailedDiagram}/>
                </Grid>
            </Grid>
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={3000}
                message={snackbarMessage}
                onClose={() => setSnackbarOpen(false)}
                action={<IconButton onClick={() => setSnackbarOpen(false)}><CloseIcon/></IconButton>}
            />
        </Box>
    );
}

export default DiagramDetails;
