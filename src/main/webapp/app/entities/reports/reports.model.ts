import { BaseEntity } from './../../shared';

export class Reports implements BaseEntity {
    constructor(
        public id?: number,
        public reportName?: string,
        public reportType?: string,
    ) {
    }
}
