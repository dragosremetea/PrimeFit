export class Training {
    id!: number;
    name!: string;
    pdfUrl: string = "pdf";
    duration!: number;
    trainingIntensity!: TrainingIntensity;
}

export enum TrainingIntensity {
    LOW,
    MEDIUM,
    EXTREME
}