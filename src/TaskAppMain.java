
import java.util.*;

// ---------------------- User Class ----------------------
class User {
    private int id;
    private String name;
    private String role;
    private String email;

    public User(int id, String name, String role, String email) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getEmail() { return email; }

    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setEmail(String email) { this.email = email; }

    public String displayDetails() {
        return "User[ID=" + id + ", Name=" + name + ", Role=" + role + ", Email=" + email + "]";
    }
}

// ---------------------- Manager Class (Inheritance + Overriding) ----------------------
class Manager extends User {
    private String department;

    public Manager(int id, String name, String email, String department) {
        super(id, name, "Manager", email);
        this.department = department;
    }

    @Override
    public String displayDetails() {
        return "Manager[ID=" + getId() + ", Name=" + getName() + ", Department=" + department + ", Email=" + getEmail() + "]";
    }

    public void approveTask(Task task) {
        task.setStatus("APPROVED");
    }
}

// ---------------------- Task Class ----------------------
class Task {
    private int id;
    private String title;
    private String description;
    private String status;
    private User assignedUser;

    public Task(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = "TODO";
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public User getAssignedUser() { return assignedUser; }

    public void setStatus(String status) { this.status = status; }
    public void assignUser(User user) { this.assignedUser = user; }

    public String displayTask() {
        return "Task[ID=" + id + ", Title=" + title + ", Status=" + status +
               ", AssignedTo=" + (assignedUser != null ? assignedUser.getName() : "Unassigned") + "]";
    }
}

// ---------------------- Project Class ----------------------
class Project {
    private int projectId;
    private String name;
    private String deadline;
    private List<Task> tasks;
    private List<User> teamMembers;

    public Project(int projectId, String name, String deadline) {
        this.projectId = projectId;
        this.name = name;
        this.deadline = deadline;
        this.tasks = new ArrayList<>();
        this.teamMembers = new ArrayList<>();
    }

    public int getId() { return projectId; }
    public String getName() { return name; }
    public String getDeadline() { return deadline; }
    public List<Task> getTasks() { return tasks; }
    public List<User> getTeamMembers() { return teamMembers; }

    public void addTask(Task task) { tasks.add(task); }
    public void addTeamMember(User user) { teamMembers.add(user); }

    public String displayProject() {
        return "Project[ID=" + projectId + ", Name=" + name + ", Deadline=" + deadline + "]";
    }
}

// ---------------------- TaskManager Class ----------------------
class TaskManager {
    private Map<Integer, User> users = new HashMap<>();
    private Map<Integer, Project> projects = new HashMap<>();
    private Map<Integer, Task> tasks = new HashMap<>();

    public void addUser(User user) { users.put(user.getId(), user); }
    public void addProject(Project project) { projects.put(project.getId(), project); }
    public void addTask(Task task) { tasks.put(task.getId(), task); }

    // Method Overloading → Assign task with/without deadline (dummy example)
    public void assignTask(int taskId, int userId) {
        Task task = tasks.get(taskId);
        User user = users.get(userId);
        if (task != null && user != null) task.assignUser(user);
    }
    public void assignTask(int taskId, int userId, String deadline) {
        assignTask(taskId, userId); // reuse
        System.out.println("Task " + taskId + " assigned with deadline: " + deadline);
    }

    public void addTaskToProject(int taskId, int projectId) {
        Task task = tasks.get(taskId);
        Project project = projects.get(projectId);
        if (task != null && project != null) project.addTask(task);
    }

    public void addUserToProject(int userId, int projectId) {
        User user = users.get(userId);
        Project project = projects.get(projectId);
        if (user != null && project != null) project.addTeamMember(user);
    }

    public List<Task> listTasksByProject(int projectId) {
        Project project = projects.get(projectId);
        return project != null ? project.getTasks() : new ArrayList<>();
    }

    public List<Task> listTasksByUser(int userId) {
        List<Task> result = new ArrayList<>();
        for (Task t : tasks.values()) {
            if (t.getAssignedUser() != null && t.getAssignedUser().getId() == userId) {
                result.add(t);
            }
        }
        return result;
    }
}

// ---------------------- Main Class ----------------------
public class TaskAppMain {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();

        // Create Users
        User u1 = new User(1, "Alice", "Developer", "alice@mail.com");
        User u2 = new User(2, "Bob", "Tester", "bob@mail.com");
        Manager m1 = new Manager(3, "Charlie", "charlie@mail.com", "IT");

        tm.addUser(u1);
        tm.addUser(u2);
        tm.addUser(m1);

        // Create Projects
        Project p1 = new Project(101, "Project A", "2025-12-31");
        Project p2 = new Project(102, "Project B", "2026-06-30");
        tm.addProject(p1);
        tm.addProject(p2);

        // Add Users to Project
        tm.addUserToProject(u1.getId(), p1.getId());
        tm.addUserToProject(u2.getId(), p1.getId());
        tm.addUserToProject(m1.getId(), p2.getId());

        // Create Tasks
        Task t1 = new Task(201, "Design Module", "Design the core module");
        Task t2 = new Task(202, "Test Module", "Test the implemented module");

        tm.addTask(t1);
        tm.addTask(t2);

        // Assign Tasks
        tm.assignTask(t1.getId(), u1.getId());
        tm.assignTask(t2.getId(), u2.getId(), "2025-10-01");

        // Add tasks to projects
        tm.addTaskToProject(t1.getId(), p1.getId());
        tm.addTaskToProject(t2.getId(), p2.getId());

        // Update task status
        t1.setStatus("IN PROGRESS");
        t2.setStatus("DONE");

        // List tasks by project
        System.out.println("\nTasks in Project A:");
        tm.listTasksByProject(p1.getId()).forEach(t -> System.out.println(t.displayTask()));

        System.out.println("\nTasks in Project B:");
        tm.listTasksByProject(p2.getId()).forEach(t -> System.out.println(t.displayTask()));

        // List tasks by user
        System.out.println("\nTasks assigned to Alice:");
        tm.listTasksByUser(u1.getId()).forEach(t -> System.out.println(t.displayTask()));

        System.out.println("\nTasks assigned to Bob:");
        tm.listTasksByUser(u2.getId()).forEach(t -> System.out.println(t.displayTask()));
    }
}
