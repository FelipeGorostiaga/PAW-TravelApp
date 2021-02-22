import {ComponentFixture, TestBed} from "@angular/core/testing";
import {ForbiddenComponent} from "./forbidden.component";


describe('ForbiddenComponent', () => {
    let component: ForbiddenComponent;
    let fixture: ComponentFixture<ForbiddenComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [ForbiddenComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(ForbiddenComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should render page title in a h1 tag', () => {
        const fixture = TestBed.createComponent(ForbiddenComponent);
        fixture.detectChanges();
        const compiled = fixture.debugElement.nativeElement;
        expect(compiled.querySelector('h1').textContent).toContain('Oops!');
    });

});
