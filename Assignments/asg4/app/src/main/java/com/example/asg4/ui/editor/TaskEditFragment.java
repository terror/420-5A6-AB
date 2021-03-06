package com.example.asg4.ui.editor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.asg4.R;
import com.example.asg4.databinding.FragmentTaskEditBinding;
import com.example.asg4.model.Action;
import com.example.asg4.model.Priority;
import com.example.asg4.model.Task;
import com.example.asg4.ui.TasksActivity;
import com.example.asg4.ui.util.DatePickerDialogFragment;
import com.example.asg4.viewmodel.TaskEditViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Stack;

public class TaskEditFragment extends Fragment {
  // Generated fragment binding
  private FragmentTaskEditBinding binding;
  // Activity reference
  private TasksActivity tasksActivity;
  // History stack
  private Stack<Task> history;
  // The task to manipulate in memory
  private Task task;
  // Map radio button ID's -> priority variants
  private HashMap<Integer, Priority> PRIORITY_RADIO_BUTTON_ID_MAP = new HashMap() {{
    put(R.id.priority_high_radioButton, Priority.HIGH);
    put(R.id.priority_medium_radioButton, Priority.MEDIUM);
    put(R.id.priority_low_radioButton, Priority.LOW);
  }};

  // View model fields
  private Action action;
  private long id;

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Overridden methods                                                       ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    tasksActivity = (TasksActivity) context;
    tasksActivity.setTaskEditFragment(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentTaskEditBinding.inflate(inflater, container, false);

    // Initialize the task history stack
    history = new Stack();
    task = new Task();
    history.add(task);

    // Set option menu for the checkbox
    setHasOptionsMenu(true);

    return binding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
    // Inflate the menu; this adds items to the action bar if it is present.
    inflater.inflate(R.menu.menu_task_edit, menu);

    // Grab the app bar's `CheckBox` by ID.
    CheckBox checkBox = (CheckBox)
      menu
        .findItem(R.id.app_bar_checkbox)
        .getActionView();

    // Set an onCheckedChange listener to the app bar's checkbox
    // displaying the debug window if it is checked.
    checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
      if (isChecked)
        displayTask();
    });
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // set all ui event listeners
    binding.dateImageButton.setOnClickListener(_view -> chooseDate());
    binding.dateToolbar.setOnClickListener(_view -> editDate());
    binding.dateToolbarClose.setOnClickListener(_view -> closeDate());
    binding.descriptionEditTextTextMultiLine.addTextChangedListener(onDescriptionTextChanged());
    binding.priorityCloseImageButton.setOnClickListener(_view -> closePriority());
    binding.priorityImageButton.setOnClickListener(_view -> choosePriority());
    binding.priorityToolbarRadioGroup.setOnCheckedChangeListener((radioGroup, id) -> changePriority(radioGroup));
    binding.undoImageButton.setOnClickListener(_view -> undo());

    // handle action given the current view model state
    handleAction(tasksActivity.getTaskEditViewModel());
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Getters                                                                  ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  public Task getCurrentTask() {
    // If there's no history then
    // return the `Task` instance on this.
    if (history.isEmpty())
      return task;

    // The current task is at the top of the stack
    return history.peek().copy();
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Date methods                                                             ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  private void chooseDate() {
    // Display the DatePickerDialog
    DatePickerDialogFragment.create(new DatePickerDialogOnSetDateHandler(this))
      .show(getParentFragmentManager(), "datePicker");
  }

  private void editDate() {
    // Get the current date
    Calendar curr = Calendar.getInstance();
    curr.setTime(getDateSelection());

    // Display the DatePickerDialog with the currently selected date
    DatePickerDialogFragment.create(
      curr.getTime(),
      new DatePickerDialogOnSetDateHandler(this, curr.get(Calendar.HOUR), curr.get(Calendar.MINUTE))
    ).show(getParentFragmentManager(), "datePicker");
  }

  public void displayDate(Calendar calendar) {
    // Validate the chosen date
    if (calendar.getTime().before(new Date())) {
      // Display a new alert dialog is the selected date
      // is before the current date
      new AlertDialog.Builder(getContext())
        .setTitle("Invalid due date")
        .setMessage("Please select a date in the future.")
        .setNegativeButton(android.R.string.ok, null)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
      return;
    }

    // Disable the date image button
    binding.dateImageButton.setEnabled(false);

    // Set the date toolbar's text to the calendars date string
    binding.dateToolbarTextView.setText(calendar.getTime().toString());

    // Make the date toolbar visible
    binding.dateToolbarLayout.setVisibility(View.VISIBLE);

    // Add to the history
    addTask(getCurrentTask().setDue(calendar.getTime()));
  }

  public void hideDate() {
    // Enable the date image button
    binding.dateImageButton.setEnabled(true);

    // Clear the date toolbar's text view
    binding.dateToolbarTextView.setText("");

    // Hide the date toolbar
    binding.dateToolbarLayout.setVisibility(View.GONE);
  }

  private void closeDate() {
    // Hide the date toolbar
    hideDate();

    // Add to the history
    addTask(getCurrentTask().setDue(null));
  }

  private Date getDateSelection() {
    // Grab the current date selection as a string
    String date = binding.dateToolbarTextView.getText().toString();

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

    // Parse the date string
    try {
      calendar.setTime(Objects.requireNonNull(formatter.parse(date)));
    } catch (ParseException e) {
      calendar.setTime(new Date());
    }

    return calendar.getTime();
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Priority methods                                                         ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  private void choosePriority() {
    // Disable the priority image button
    binding.priorityImageButton.setEnabled(false);

    // Make the priority toolbar visible
    binding.priorityToolbarLayout.setVisibility(View.VISIBLE);

    // Set the `High` priority as default
    binding.priorityToolbarRadioGroup.check(R.id.priority_high_radioButton);
  }

  private void changePriority(RadioGroup group) {
    // Grab the currently checked radio button
    RadioButton radioButton = group.findViewById(group.getCheckedRadioButtonId());

    // Ensure it's checked and non-null
    if (radioButton != null && radioButton.isChecked()) {
      Priority priority = PRIORITY_RADIO_BUTTON_ID_MAP.get(radioButton.getId());
      // Only add to the history if the current priority isn't equal to the last
      if (getCurrentTask().getPriority() != priority)
        addTask(getCurrentTask().setPriority(priority));
    }
  }

  private void hidePriority() {
    // Enable the priority image button
    binding.priorityImageButton.setEnabled(true);

    // Clear the priority radio group's check
    binding.priorityToolbarRadioGroup.clearCheck();

    // Hide the priority toolbar
    binding.priorityToolbarLayout.setVisibility(View.GONE);
  }

  private void closePriority() {
    // Hide the priority toolbar
    hidePriority();

    // Add to the history
    addTask(getCurrentTask().setPriority(Priority.NONE));
  }

  private void checkPriority(Priority priority) {
    // Check the radio button with ID corresponding to the passed
    // in `Priority` variant.
    for (Integer id : PRIORITY_RADIO_BUTTON_ID_MAP.keySet()) {
      if (PRIORITY_RADIO_BUTTON_ID_MAP.get(id).equals(priority))
        binding.priorityToolbarRadioGroup.check(id);
    }
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Description methods                                                      ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  private TextWatcher onDescriptionTextChanged() {
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        addTask(getCurrentTask().setDescription(s.toString()));
      }

      @Override
      public void afterTextChanged(Editable editable) {
      }
    };
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Undo method(s)                                                           ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  private void undo() {
    // If there's no previous task, hide everything
    if (history.size() < 2) {
      binding.descriptionEditTextTextMultiLine.setText("");
      hideDate();
      hidePriority();
      return;
    }

    // Grab the previous task
    Task prev = getPreviousTask();

    // Set the description
    if (prev.getDescription() != binding.descriptionEditTextTextMultiLine.getText().toString())
      binding.descriptionEditTextTextMultiLine.setText(prev.getDescription());

    // If the current due date is null and the previous one
    // is set, make sure the date toolbar is shown.
    if (prev.getDue() != null && binding.dateToolbarLayout.getVisibility() == View.GONE) {
      binding.dateToolbarLayout.setVisibility(View.VISIBLE);
      binding.dateToolbarTextView.setText(prev.getDue().toString());
    }
    // If the previous due date is null, hide the toolbar.
    else if (prev.getDue() == null) {
      hideDate();
    }
    // Set the date text to the previous one
    else {
      binding.dateToolbarTextView.setText(prev.getDue().toString());
    }

    // If the current priority is null and the previous one
    // is set, make sure the priority toolbar is shown.
    if (prev.getPriority() != Priority.NONE && binding.priorityToolbarLayout.getVisibility() == View.GONE) {
      binding.priorityToolbarLayout.setVisibility(View.VISIBLE);
      checkPriority(prev.getPriority());
    }
    // If the previous priority is null, hide the toolbar.
    else if (prev.getPriority() == Priority.NONE) {
      hidePriority();
    }
    // Check the corresponding radio button (high, medium or low)
    else {
      checkPriority(prev.getPriority());
    }
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Action handlers                                                          ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  public void handleAction(TaskEditViewModel item) {
    // set the current action
    action = item.getAction();

    // if we wish to edit a task, set it's fields on the UI
    if (action.equals(Action.EDIT)) {
      // set the current id
      id = item.getTask().getId();

      // set the description
      binding.descriptionEditTextTextMultiLine.setText(item.getTask().getDescription());

      // set due date text without validation
      if (item.getTask().getDue() != null) {
        // Disable the date image button
        binding.dateImageButton.setEnabled(false);
        // Set the date toolbar's text to the calendars date string
        binding.dateToolbarTextView.setText(item.getTask().getDue().toString());
        // Make the date toolbar visible
        binding.dateToolbarLayout.setVisibility(View.VISIBLE);
        // Add to the history
        addTask(getCurrentTask().setDue(item.getTask().getDue()));
      }

      // set priority
      if (item.getTask().getPriority() != null) {
        // `checkPriority` handles the history
        checkPriority(item.getTask().getPriority());
        // Make the priority toolbar visible
        binding.priorityToolbarLayout.setVisibility(View.VISIBLE);
      }

      // once the modifiable fields are set, push the current task onto the history
      addTask(item.getTask());
    }
  }

  public void handleBackPressed() {
    // set properties on the task list
    // view model and notify listeners
    tasksActivity.getTaskListViewModel()
      .setTask(getCurrentTask())
      .setAction(action)
      .setId(id)
      .notifyChange();

    // navigate to the `list` fragment
    navigateToList();
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Navigation                                                               ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  private void navigateToList() {
    // show the `add` button
    tasksActivity.getBinding().fab.setVisibility(View.VISIBLE);

    // navigate to `list` fragment by popping off the stack
    Navigation
      .findNavController(getActivity(), R.id.nav_host_fragment_content_main)
      .popBackStack();
  }

  public void navigateBack() {
    // validate the current task and perform navigation on `ok`
    // if the task is valid, notify the list fragment
    if (validateCurrentTask(((dialogInterface, i) -> navigateToList())))
      handleBackPressed();
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Validation                                                               ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  private boolean validateCurrentTask(DialogInterface.OnClickListener listener) {
    // get the current task
    Task currentTask = getCurrentTask();

    // validate the description in either case, if the task is null
    // or does not contain a description, display an alert dialog
    if (currentTask == null || currentTask.getDescription() == null || currentTask.getDescription().equals("")) {
      new AlertDialog.Builder(getActivity())
        .setTitle("Empty description")
        .setMessage("Description is empty, no task will be added or modified.")
        .setNegativeButton(android.R.string.ok, listener)
        .show();

      // the current task isn't valid
      return false;
    }

    // the current task is valid
    return true;
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Task methods                                                             ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  public Task getPreviousTask() {
    // The task on top of the stack is the current one,
    // pop it off and return the one under it while keeping the
    // current task in sync.
    history.pop();
    task = history.pop();
    return task;
  }

  public void addTask(Task task) {
    // Push a new task onto the history
    history.push(task);
  }

  private void displayTask() {
    // Display the current `Task` instance in a debug window.
    new AlertDialog.Builder(getContext())
      .setTitle("Debug")
      .setMessage(getCurrentTask().toString())
      .setNegativeButton(android.R.string.ok, null)
      .setNegativeButton(android.R.string.cancel, null)
      .setIcon(android.R.drawable.ic_dialog_alert)
      .show();
  }
}
