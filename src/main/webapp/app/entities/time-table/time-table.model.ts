import { BaseEntity } from './../../shared';

export class TimeTable implements BaseEntity {
    constructor(
        public id?: number,
        public startingFrom?: string,
        public endingFrom?: string,
        public departure?: string,
        public arrival?: string,
        public frequency?: string,
        public busNo?: string,
        public routes?: BaseEntity[],
        public transportManager?: BaseEntity,
    ) {
    }
}
