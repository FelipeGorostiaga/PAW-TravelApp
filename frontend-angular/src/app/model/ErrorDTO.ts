export class ErrorDTO {

    message: string;
    field: string;

    constructor(message: string, field: string) {
        this.message = message;
        this.field = field;
    }
}
