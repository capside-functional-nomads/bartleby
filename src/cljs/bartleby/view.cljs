(ns bartleby.view
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [goog.string :as gstring]
            [goog.string.format]
            [bartleby.events]
            [bartleby.subs :as subs]))

(defn task-clicked [task]
  (.info js/console "Clicked task" (:id task))
  (re-frame/dispatch [:complete-task (:id task)]))
;;  (re-frame/dispatch [:task-toggle-done (:id task) (not (:done task))]))

(defn todo-item
  [{id :id desc :description done :done :as task}]
  [:a.list-group-item {:key id
                        :on-click #(task-clicked task)} (str id ". " desc)
   (when done
     [:span.glyphicon.glyphicon-ok {:aria-hidden true}])])

(defn bartleby-warning [remaining]
  (let [c (count remaining)]
    (if (= c 0)
      [:p "Bartleby, now you can proceed to lean about the Curry-Howard isomorphism"]
      [:p (str "Bartleby you've got " c " tasks left")])))

(defn status-item
  [idx item]
  (let [alert-type (if (= (:status item) :error) "alert-danger" "alert-success")
        heading (if (= (:status item) :error) "REST Error" "REST Ok")
        text (:message item)]
    [:row
      {:key (gstring/format "status%02d" idx)}
     [:div.alert.alert-dismissible
      {:class alert-type
       :role "alert" :on-click #(re-frame/dispatch [:close-alert idx])}
      [:strong heading]
      [:p text]]]))

(defn statuses-list
  []
  (let [rest-statuses (re-frame/subscribe [:rest])]
    [:div
     (map-indexed status-item @rest-statuses)]))
        

(defn main-view []
  (let [tasks (re-frame/subscribe [:tasks])
        rest-status (re-frame/subscribe [:rest])
        remaining (filter (fn [t] (not (:done t))) @tasks)]
    [:div
     [:h1 "Remaining tasks"]
     [:div.list-group
      (map todo-item @tasks)]
     [bartleby-warning remaining]]))

(defn layout [title content status]
  [:div
   [:nav.navbarn.navbar-fixed-top.navbar-inverse
    [:div.container
     [:div.navbar-header
      [:button.navbar-toggle.collapsed {:type "button" :data-toggle "collapse" :data-target "#navbar" :aria-expanded false :aria-controls "navbar"}
       [:span.sr-only "Toggle navigation"]
       [:span.icon-bar]
       [:span.icon-bar]
       [:span.icon-bar]]]
     [:div#navbar.collapse.navbar-collapse
      [:ul.nav.navbar-nav
       [:li.active
        [:a {:href "#"} title]]]]]]
   [:div.container-fluid
    [:div.row
     [:div.col-md-6.col-md-offset-3
      [content]]
     [:div.col-md-2.col-md-offset-1
      [status]]]]])

(defn ui[]
  (layout "Bartleby" main-view statuses-list))
