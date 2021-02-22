import {ComponentFixture, TestBed} from "@angular/core/testing";
import {AboutComponent} from "./about.component";


describe('ForbiddenComponent', () => {
    let component: AboutComponent;
    let fixture: ComponentFixture<AboutComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [AboutComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(AboutComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should render page title in a h3 tag', () => {
        const fixture = TestBed.createComponent(AboutComponent);
        fixture.detectChanges();
        const compiled = fixture.debugElement.nativeElement;
        expect(compiled.querySelector('h3').textContent).toContain('Felipe Gorostiaga');
    });

});
