export type BpmnDiagramModel = {
    id: string
    name: string
    businessKey: string
    filename: string
    version: number,
    calledProcesses: BpmnDiagramModel[],
    commentText: string,
    commentAuthor: string,
    customDiagram: boolean
}
