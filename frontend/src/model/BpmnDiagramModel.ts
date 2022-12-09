import {CommentModel} from "./CommentModel";

export type BpmnDiagramModel = {
    id: string
    name: string
    businessKey: string
    filename: string
    version: number
    comments: CommentModel[]
    calledDiagrams: {
        calledDiagramId: string,
        calledFromActivities: string[]
    }[]
    template: boolean
    customDiagram: boolean
}
