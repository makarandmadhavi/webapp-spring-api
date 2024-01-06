#!/bin/bash

# Check if the required parameter is provided
if [ -z "$1" ]; then
  echo "Usage: $0 <autoscaling-group-name>"
  exit 1
fi

autoscaling_group_name="$1"
iteration=1

while true; do
    refresh_status=$(aws autoscaling describe-instance-refreshes --auto-scaling-group-name "$autoscaling_group_name" --query 'InstanceRefreshes[0].Status' --output text)

    echo "Iteration $iteration:"
    echo $refresh_status

    case "$refresh_status" in
          "Pending")
            echo "Instance refresh is pending."
            ;;
          "InProgress")
            echo "Instance refresh is in progress."
            ;;
          "Successful")
            echo "Instance refresh completed successfully."
            exit 0
            break
            ;;
          "Failed")
            echo "Instance refresh failed."
            exit 1
            ;;
          "Cancelling")
            echo "Instance refresh is being cancelled."
            ;;
          "Cancelled")
            echo "Instance refresh is cancelled."
            exit 1
            break
            ;;
          "RollbackInProgress")
            echo "Instance refresh is being rolled back."
            ;;
          "RollbackFailed")
            echo "Rollback failed to complete. You can troubleshoot using the status reason and the scaling activities."
            exit 1
            ;;
          "RollbackSuccessful")
            echo "Rollback completed successfully."
            break
            ;;
          *)
            echo "Unknown instance refresh status: $refresh_status"
            ;;
    esac
    sleep 20

    ((iteration++))
done
