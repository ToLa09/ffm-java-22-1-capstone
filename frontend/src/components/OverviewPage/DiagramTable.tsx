import React, {Dispatch, SetStateAction, useState} from 'react';
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import DiagramTableRow from "./DiagramTableRow";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import SyncIcon from '@mui/icons-material/Sync';
import Button from "@mui/material/Button";
import axios from "axios";
import CloseIcon from "@mui/icons-material/Close";
import {Box, FormControl, Grid, InputLabel, MenuItem, Select, Snackbar, TextField, Typography} from "@mui/material";
import IconButton from "@mui/material/IconButton";

type DiagramTableProps = {
    setDetailedDiagramId: Dispatch<SetStateAction<string>>
    bpmnDiagrams: BpmnDiagramModel[]
    fetchDiagrams: () => void
}

function DiagramTable(props: DiagramTableProps) {
    const [snackbarRefreshOpen, setSnackbarRefreshOpen] = useState<boolean>(false)
    const [filterValue, setFilterValue] = useState<string>("")
    const [filterProperty, setFilterProperty] = useState<string>("name")

    const fetchCamundaDiagrams = () => {
        axios.post("/api/camundaprocesses")
            .then(response => {
                if (response.status === 204) {
                    setSnackbarRefreshOpen(true)
                }
                props.fetchDiagrams()
            })
            .catch(error => console.error(error))
    }

    return (
        <Box m={5}>
            <Box mb={3}>
                <Button onClick={fetchCamundaDiagrams} variant="outlined" color="secondary">
                    <SyncIcon/><Typography>Get diagrams from Camunda</Typography>
                </Button>
            </Box>
            <Box mb={3}>
                <Grid container spacing={2}>
                    <Grid item xs>
                        <TextField
                            fullWidth
                            label="Filter BPMN Diagrams"
                            id="filterDiagrams"
                            value={filterValue}
                            onChange={(event) => setFilterValue(event.target.value)}
                        />
                    </Grid>
                    <Grid item xs={4}>
                        <FormControl fullWidth>
                            <InputLabel id="select-filter">Filter by</InputLabel>
                            <Select
                                labelId="select-filter"
                                id="select-filter"
                                label="Filter by"
                                value={filterProperty}
                                onChange={(event) => setFilterProperty(event.target.value)}
                            >
                                <MenuItem value={"name"}>Name</MenuItem>
                                <MenuItem value={"filename"}>Filename</MenuItem>
                                <MenuItem value={"comments"}>Comments</MenuItem>
                            </Select>
                        </FormControl>
                    </Grid>
                </Grid>
            </Box>
            <TableContainer component={Paper}>
                <Table sx={{minWidth: 650}} size="small" aria-label="BPMN-Diagram-table" className="diagramtable">
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell align="left">Filename</TableCell>
                            <TableCell align="center">latest Version</TableCell>
                            <TableCell align="center">latest Comments</TableCell>
                            <TableCell align="center">Call Activities/ Calls from other Processes</TableCell>
                            <TableCell align="center">Type</TableCell>
                            <TableCell align="center">Changeability</TableCell>
                            <TableCell></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {props.bpmnDiagrams
                            .filter(diagram => {
                                const filterRegExp = new RegExp('^.*' + filterValue.replace(/\*/g, '.*') + '.*$')

                                switch (filterProperty) {
                                    case "name":
                                        return filterRegExp.test(diagram.name)
                                    case "filename":
                                        return filterRegExp.test(diagram.filename)
                                    case "comments":
                                        return filterRegExp.test(JSON.stringify(diagram.comments))
                                }
                                return diagram;
                            })
                            .map(diagram => (
                                <DiagramTableRow
                                    key={diagram.id}
                                    diagram={diagram}
                                    bpmnDiagrams={props.bpmnDiagrams}
                                    fetchDiagrams={props.fetchDiagrams}
                                    setDetailedDiagramId={props.setDetailedDiagramId}
                                ></DiagramTableRow>
                            ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <Snackbar
                open={snackbarRefreshOpen}
                autoHideDuration={3000}
                message="Updated Diagramlist (Fetched Data from Camunda Engine)"
                onClose={() => setSnackbarRefreshOpen(false)}
                action={<IconButton onClick={() => setSnackbarRefreshOpen(false)}><CloseIcon/></IconButton>}
            />
        </Box>
    );
}

export default DiagramTable;
