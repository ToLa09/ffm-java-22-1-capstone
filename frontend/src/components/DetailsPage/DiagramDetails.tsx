import React, {Dispatch, SetStateAction, useState} from 'react';
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import {Button, Card, Grid, Snackbar,} from "@mui/material";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";
import HistoryList from "./HistoryList";
import CommentList from "./CommentList";
import PropertiesList from "./PropertiesList";

type DiagramDetailsProps = {
    setTab: Dispatch<SetStateAction<string>>
    detailedDiagram: BpmnDiagramModel
    setDetailedDiagram: Dispatch<SetStateAction<BpmnDiagramModel>>
    fetchDiagrams: () => void
}

function DiagramDetails(props: DiagramDetailsProps) {

    const [snackbarOpen, setSnackbarOpen] = useState<boolean>(false)
    const [snackbarMessage, setSnackbarMessage] = useState<string>("")

    return (
        <>
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <Button
                        variant="outlined"
                        color="secondary"
                        onClick={() => props.setTab("Overview")}
                    >
                        Go back to Overview
                    </Button>
                </Grid>
                <Grid item xs={4}>
                    <PropertiesList
                        detailedDiagram={props.detailedDiagram}
                        fetchDiagrams={props.fetchDiagrams}
                        setSnackbarOpen={setSnackbarOpen}
                        setSnackbarMessage={setSnackbarMessage}
                        setDetailedDiagram={props.setDetailedDiagram}
                    />
                </Grid>
                <Grid item xs>
                    <Card>
                        <CommentList
                            detailedDiagram={props.detailedDiagram}
                        />
                    </Card>
                </Grid>
                <Grid item xs={12}>
                    <Card>
                        <HistoryList
                            latestDiagram={props.detailedDiagram}
                            setSnackbarOpen={setSnackbarOpen}
                            setSnackbarMessage={setSnackbarMessage}
                            fetchDiagrams={props.fetchDiagrams}
                            setTab={props.setTab}
                        />
                    </Card>
                </Grid>
            </Grid>
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={3000}
                message={snackbarMessage}
                onClose={() => setSnackbarOpen(false)}
                action={<IconButton onClick={() => setSnackbarOpen(false)}><CloseIcon/></IconButton>}
            />

        </>
    );
}

export default DiagramDetails;
