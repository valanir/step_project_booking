package step.project.consoleApp.myConsoleMenu;

@FunctionalInterface
public interface ItemPreparationMethod {
    void preparation(MenuItem menuItem) throws Exception;
}
