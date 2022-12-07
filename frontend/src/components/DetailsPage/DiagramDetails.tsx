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
    detailedDiagramId: string
    setDetailedDiagramId: Dispatch<SetStateAction<string>>
    bpmnDiagrams: BpmnDiagramModel[]
    fetchDiagrams: () => void
}

function DiagramDetails(props: DiagramDetailsProps) {
    const {id} = useParams()
    const navigate = useNavigate()

    const [snackbarOpen, setSnackbarOpen] = useState<boolean>(false)
    const [snackbarMessage, setSnackbarMessage] = useState<string>("")
    const [diagram, setDiagram] = useState<BpmnDiagramModel>({
        id: ""
        , name: "-"
        , businessKey: "-"
        , filename: "-"
        , version: 1
        , comments: []
        , customDiagram: true
    })

    const getDiagramFromListById = () => {
        props.bpmnDiagrams.forEach(diagram => {
            if (id === diagram.id) {
                props.setDetailedDiagramId(id)
                setDiagram(diagram)
            }
        })
    }

    useEffect(getDiagramFromListById, [id, props])


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
                        detailedDiagram={diagram}
                        setDetailedDiagram={setDiagram}
                        fetchDiagrams={props.fetchDiagrams}
                        setSnackbarOpen={setSnackbarOpen}
                        setSnackbarMessage={setSnackbarMessage}
                    />
                </Grid>
                <Grid item xs>
                    <CommentList
                        detailedDiagram={diagram}
                        fetchDiagrams={props.fetchDiagrams}
                    />
                </Grid>
                <Grid item xs={12}>
                    <HistoryList
                        latestDiagram={diagram}
                        setSnackbarOpen={setSnackbarOpen}
                        setSnackbarMessage={setSnackbarMessage}
                        fetchDiagrams={props.fetchDiagrams}
                    />
                </Grid>
                <Grid item xs={12}>
                    <BpmnViewer diagram={diagram}/>
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
